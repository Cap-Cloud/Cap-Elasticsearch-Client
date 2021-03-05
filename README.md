# Cap-Elasticsearch-Client

A IDEA Plugin for Elasticsearch, allows accessing to Elasticsearch cluster, browse and edit your data, execute REST API requests, execute SQL query.

Some code implementations refer to some excellent projects.

For example:

[Elasticsearch](https://plugins.jetbrains.com/plugin/14512-elasticsearch)

[Mongo Plugin](https://plugins.jetbrains.com/plugin/14512-elasticsearch)

Thanks to these projects and their authors!

## How to use

### Install

You can install it through Marketplace, or zip archive.

Then you will see like this:

![](./images/install_01.png)

### Add Cluster

You can add a cluster with two steps:

* click the menu  

![](./images/add_cluster_01.png)

* enter the cluster information

![](./images/add_cluster_02.png)

Then you can manage your cluster.

![](./images/add_cluster_03.png)

### REST API Query

You can operate your data use rest api for all versions of Elasticsearch.

* `/`

![](./images/rest_query_01.png)

* `_search` View as JSON Tree

![](./images/rest_query_02.png)

* `_search` View as Table

![](./images/rest_query_03.png)

### SQL Query

In addition, you can query your data use SQL for high version Elasticsearch.

![](./images/sql_query_01.png)

![](./images/sql_query_02.png)

Now it supports SQL syntax checking and SQL syntax highlighting.

![](./images/sql_query_03.png)

You can customize your highlight color.

![](./images/sql_query_04.png)


### Favorite API

As you can see, I only implemented a few simple menu items,  it's not enough. So you can define your own favorite api, it will display as a menu item.

* Enter item name

![](./images/favorite_api_01.png)

* Enter API informations

![](./images/favorite_api_02.png)

* Then, you will find it as a menu item

![](./images/favorite_api_03.png)

* Or use it from editor's toolbar

![](./images/favorite_api_04.png)

* It will works

![](./images/favorite_api_05.png)

* You can query it

![](./images/favorite_api_06.png)

### Internationalization

Two languages are provided by default, but some semantics are not very accurate, you can define your own display language.

* enter your language identification

![](./images/i18n_01.png)

* enter your message

![](./images/i18n_02.png)

* choose it

![](./images/i18n_03.png)

* you will see it

![](./images/i18n_04.png)

### Other

Other features are on the way...