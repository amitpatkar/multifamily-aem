const merge             = require('webpack-merge');
const common            = require('./webpack.common.js');
const path              = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const SOURCE_ROOT = __dirname + '/src/main/webpack';
const staticFiles = ['index.html', 
                    'header.html',
                    'standalone-header.html',
                    'footer.html',
                    'section100.html', 
                    'section33-33-33.html', 
                    'get-in-touch.html', 
                    "hero.html",
                    "availability-feed.html",
                    "tabbed-availability-feed.html",
                "section50-50.html",
                "section50-50-carousel.html",
            "accordions.html",
        "apartment-detail-optionA.html",
        "apartment-detail-optionA-revise.html",
        "apartment-detail-optionB-revise.html",
        "apartment-detail-optionB.html",
        "apartment-detail-optionC.html",
    "masonry.html",
    "error.html",
"gallery.html",
"rentdot-header.html",
"rentdot-search.html",
"rentdot-global-header.html",
"rentdot-contact.html",
"rentdot-properties.html",
"poi-map.html",
"locations-map.html", 
"notable-contents-carousel.html",
"property-spotlight.html", 
"amenity-list.html",
"sample-page.html",
"you-may-also-like.html"];
const pluginsArr = [];
staticFiles.forEach(filename => {
    pluginsArr.push(new HtmlWebpackPlugin({
        filename: filename,
        template: path.resolve(__dirname, SOURCE_ROOT + '/static/' + filename),
    }))
});

module.exports = env => {
    const writeToDisk = env && Boolean(env.writeToDisk);

    return merge(common, {
        mode: 'development',
        devtool: 'inline-source-map',
        performance: { hints: 'warning' },
        plugins: pluginsArr,
        devServer: {
            inline: true,
            proxy: [{
                context: ['/content', '/etc.clientlibs'],
                target: 'http://localhost:4502',
            }],
            writeToDisk,
            liveReload: !writeToDisk
        }
    });
}