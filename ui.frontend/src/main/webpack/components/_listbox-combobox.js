import {Utils} from "./_utils";
/*
*   This content is licensed according to the W3C Software License at
*   https://www.w3.org/Consortium/Legal/2015/copyright-software-and-document
*/
/**
 * @constructor
 *
 * @desc
 *  Combobox object representing the state and interactions for a combobox
 *  widget
 *
 * @param comboboxNode
 *  The DOM node pointing to the combobox
 * @param input
 *  The input node
 * @param listbox
 *  The listbox node to load results in
 * @param searchFn
 *  The search function. The function accepts a search string and returns an
 *  array of results.
 */
function ListboxCombobox (
  comboboxNode,
  input,
  listbox,
  searchFn,
  shouldAutoSelect,
  onShow,
  onHide
) {
  this.combobox = comboboxNode;
  this.input = input;
  this.listbox = listbox;
  this.searchFn = searchFn;
  this.shouldAutoSelect = shouldAutoSelect;
  this.onShow = onShow || function(){console.log('on show')};
  this.onHide = onHide || function(){console.log('on hide')};
  this.activeIndex = -1;
  this.resultsCount = 0;
  this.shown = false;
  this.hasInlineAutocomplete = input.getAttribute('aria-autocomplete') === 'both';
  this.setupEvents();
};

ListboxCombobox.prototype.setupEvents = function () {
  document.body.addEventListener('click', this.checkHide.bind(this));
  this.input.addEventListener('keyup', this.checkKey.bind(this));
  this.input.addEventListener('keydown', this.setActiveItem.bind(this));
  this.input.addEventListener('focus', this.checkShow.bind(this));
  this.input.addEventListener('blur', this.checkSelection.bind(this));
  this.listbox.addEventListener('click', this.clickItem.bind(this));
};

ListboxCombobox.prototype.checkKey = function (evt) {
  var key = evt.which || evt.keyCode;

  switch (key) {
    case Utils.KeyCode.UP:
    case Utils.KeyCode.DOWN:
    case Utils.KeyCode.ESC:
    case Utils.KeyCode.RETURN:
      evt.preventDefault();
      return;
    default:
      this.updateResults(false);
  }

  if (this.hasInlineAutocomplete) {
    switch (key) {
      case Utils.KeyCode.BACKSPACE:
        return;
      default:
        this.autocompleteItem();
    }
  }
};

ListboxCombobox.prototype.updateResults = function (shouldShowAll) {
  var searchString = this.input.value;
  var results = this.searchFn(searchString);

  this.hideListbox();

  if (!shouldShowAll && !searchString) {
    results = {locationResults:[], propertyResults: []};
  }

  const locations =  results['locationResults'] || [];
  const properties = results['propertyResults'] || [];
  const combinedResults = locations.concat(properties);
  if (searchString && searchString.trim().length >= 2) {
    if (combinedResults.length ) {
      if (locations.length) {
        var locationGroup = document.createElement('ul');
        locationGroup.className = 'suggestion-group';
        locationGroup.setAttribute('role', 'group');
        locationGroup.setAttribute('id', 'location-group');
        locationGroup.setAttribute('aria-labelledby', 'location_label');
        var locationLabel= document.createElement('li');
        locationLabel.className = 'suggestion-label';
        locationLabel.setAttribute('role', 'presentation');
        locationLabel.setAttribute('id', 'location_label');
        locationLabel.innerText="Location";
        locationGroup.appendChild(locationLabel);
        this.listbox.appendChild(locationGroup);
      }
      if (properties.length) {
        var propertyGroup = document.createElement('ul');
        propertyGroup.className = 'suggestion-group';
        propertyGroup.setAttribute('role', 'group');
        propertyGroup.setAttribute('id', 'property-group');
        propertyGroup.setAttribute('aria-labelledby', 'property_label');
        var propertyLabel= document.createElement('li');
        propertyLabel.className = 'suggestion-label';
        propertyLabel.setAttribute('role', 'presentation');
        propertyLabel.setAttribute('id', 'property_label');
        propertyLabel.innerText='Property';
        propertyGroup.appendChild(propertyLabel);
        this.listbox.appendChild(propertyGroup);
      }
      for (var i = 0; i < combinedResults.length; i++) {
        var resultItem = document.createElement('li');
        resultItem.className = 'suggestion-option';
        resultItem.setAttribute('role', 'option');
        resultItem.setAttribute('id', 'suggestion-option-' + i);
        resultItem.innerHTML = combinedResults[i].replace(searchString,`<strong>${searchString}</strong>`);
        if (this.shouldAutoSelect && i === 0) {
          resultItem.setAttribute('aria-selected', 'true');
          Utils.addClass(resultItem, 'focused');
          this.activeIndex = 0;
        }
        if (i < locations.length) {
          this.listbox.querySelector('#location-group').appendChild(resultItem);
        } else {
          this.listbox.querySelector('#property-group').appendChild(resultItem);
        }
      }
  
    } else {
        var noResultItem= document.createElement('p');
        noResultItem.className = 'suggestion-noresult';
        noResultItem.setAttribute('role', 'status');
        noResultItem.innerText = 'No result matching';
        this.listbox.appendChild(noResultItem);
    }
    Utils.removeClass(this.listbox, 'hidden');
    this.combobox.setAttribute('aria-expanded', 'true');
    this.resultsCount = results.length;
    this.shown = true;
    this.onShow();
  }

};

ListboxCombobox.prototype.setActiveItem = function (evt) {
  var key = evt.which || evt.keyCode;
  var activeIndex = this.activeIndex;

  if (key === Utils.KeyCode.ESC) {
    this.hideListbox();
    setTimeout((function () {
      // On Firefox, input does not get cleared here unless wrapped in
      // a setTimeout
      this.input.value = '';
    }).bind(this), 1);
    return;
  }

  if (this.resultsCount < 1) {
    if (this.hasInlineAutocomplete && (key === Utils.KeyCode.DOWN || key === Utils.KeyCode.UP)) {
      this.updateResults(true);
    }
    else {
      return;
    }
  }

  var prevActive = this.getItemAt(activeIndex);
  var activeItem;

  switch (key) {
    case Utils.KeyCode.UP:
      if (activeIndex <= 0) {
        activeIndex = this.resultsCount - 1;
      }
      else {
        activeIndex--;
      }
      break;
    case Utils.KeyCode.DOWN:
      if (activeIndex === -1 || activeIndex >= this.resultsCount - 1) {
        activeIndex = 0;
      }
      else {
        activeIndex++;
      }
      break;
    case Utils.KeyCode.RETURN:
      activeItem = this.getItemAt(activeIndex);
      this.selectItem(activeItem);
      return;
    case Utils.KeyCode.TAB:
      this.checkSelection();
      this.hideListbox();
      return;
    default:
      return;
  }

  evt.preventDefault();

  activeItem = this.getItemAt(activeIndex);
  this.activeIndex = activeIndex;

  if (prevActive) {
    Utils.removeClass(prevActive, 'focused');
    prevActive.setAttribute('aria-selected', 'false');
  }

  if (activeItem) {
    this.input.setAttribute(
      'aria-activedescendant',
      'suggestion-option-' + activeIndex
    );
    Utils.addClass(activeItem, 'focused');
    activeItem.setAttribute('aria-selected', 'true');
    if (this.hasInlineAutocomplete) {
      this.input.value = activeItem.innerText;
    }
  }
  else {
    this.input.setAttribute(
      'aria-activedescendant',
      ''
    );
  }
};

ListboxCombobox.prototype.getItemAt = function (index) {
  return document.getElementById('suggestion-option-' + index);
};

ListboxCombobox.prototype.clickItem = function (evt) {
  if (evt.target && evt.target.nodeName == 'LI') {
    this.selectItem(evt.target);
  }
};

ListboxCombobox.prototype.selectItem = function (item) {
  if (item) {
    this.input.value = item.innerText;
    this.hideListbox();
  }
};

ListboxCombobox.prototype.checkShow = function () {
  this.updateResults(false);
};

ListboxCombobox.prototype.checkHide = function (evt) {
  if (evt.target === this.input || this.combobox.contains(evt.target)) {
    return;
  }
  this.hideListbox();
};

ListboxCombobox.prototype.hideListbox = function () {
  this.shown = false;
  this.activeIndex = -1;
  this.listbox.innerHTML = '';
  Utils.addClass(this.listbox, 'hidden');
  this.combobox.setAttribute('aria-expanded', 'false');
  this.resultsCount = 0;
  this.input.setAttribute(
    'aria-activedescendant',
    ''
  );
  this.onHide();
};

ListboxCombobox.prototype.checkSelection = function () {
  if (this.activeIndex < 0) {
    return;
  }
  var activeItem = this.getItemAt(this.activeIndex);
  this.selectItem(activeItem);
};

ListboxCombobox.prototype.autocompleteItem = function () {
  var autocompletedItem = this.listbox.querySelector('.focused');
  var inputText = this.input.value;

  if (!autocompletedItem || !inputText) {
    return;
  }

  var autocomplete = autocompletedItem.innerText;
  if (inputText !== autocomplete) {
    this.input.value = autocomplete;
    this.input.setSelectionRange(inputText.length, autocomplete.length);
  }
};

export default ListboxCombobox;
