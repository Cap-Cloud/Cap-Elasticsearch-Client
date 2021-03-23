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

### Basic Menu

There are some basic menu items.

* Cluster API

![](./images/basic_menu_cluster.png)

* Nodes API

![](./images/basic_menu_nodes.png)

* Indices API

![](./images/basic_menu_indices.png)

* Index API

![](./images/basic_menu_index.png)

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

If you have installed the [elasticsearch-sql](https://github.com/NLPchina/elasticsearch-sql) plugin, you can execute sql query like this:

![](./images/sql_query_01.png)

![](./images/sql_query_02.png)

It supports explain sql query or execute sql query that supported by [elasticsearch-sql](https://github.com/NLPchina/elasticsearch-sql) plugin.

![](./images/sql_query_03.png)

But, if you have installed the [opendistro-for-elasticsearch](https://github.com/opendistro-for-elasticsearch/sql) plugin, it will support later.

If you use x-pack-sql for sql query, you can execute sql query like this:

![](./images/sql_query_04.png)

![](./images/sql_query_05.png)

All of them supports SQL syntax checking, SQL syntax highlighting and autocomplete.

![](./images/sql_query_06.png)

You can customize your highlight color.

![](./images/sql_query_07.png)

### Simulate Kibana Query

#### DevTools

##### Console

If you like to use kibana dev tools console, you can use it like this:

![](./images/dev_tools_console_01.png)

Enter your request and execute it, you will get the response.

![](./images/dev_tools_console_02.png)

You can enter multiple request, and select which one to execute.

![](./images/dev_tools_console_03.png)

And it support autocomplete.

* Request method autocomplete.

![](./images/dev_tools_console_04.png)

* Request path autocomplete.

![](./images/dev_tools_console_05.png)

![](./images/dev_tools_console_06.png)

* Index autocomplete

![](./images/dev_tools_console_07.png)

![](./images/dev_tools_console_08.png)

You can customize your highlight color.

![](./images/dev_tools_console_09.png)

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