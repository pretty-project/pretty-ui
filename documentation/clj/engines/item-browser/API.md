
# engines.item-browser.api Clojure namespace

##### [README](../../../../README.md) > [DOCUMENTATION](../../../COVER.md) > engines.item-browser.api

### Index

- [env->count-pipeline](#env-count-pipeline)

- [env->get-pipeline](#env-get-pipeline)

- [env->item-links](#env-item-links)

- [env->pipeline-props](#env-pipeline-props)

- [env->search-pattern](#env-search-pattern)

- [env->sort-pattern](#env-sort-pattern)

- [item->parent-id](#item-parent-id)

- [item->parent-link](#item-parent-link)

- [item->path](#item-path)

- [item-id->parent-id](#item-id-parent-id)

- [item-id->parent-link](#item-id-parent-link)

- [item-id->path](#item-id-path)

### env->count-pipeline

```
@param (map) env
@param (keyword) browser-id
```

```
@usage
(env->count-pipeline {...} :my-browser)
```

```
@return (maps in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [env->count-pipeline]]))

(engines.item-browser.api/env->count-pipeline ...)
(env->count-pipeline                          ...)
```

</details>

---

### env->get-pipeline

```
@param (map) env
@param (keyword) browser-id
```

```
@usage
(env->get-pipeline {...} :my-browser)
```

```
@return (maps in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [env->get-pipeline]]))

(engines.item-browser.api/env->get-pipeline ...)
(env->get-pipeline                          ...)
```

</details>

---

### env->item-links

```
@param (map) env
@param (keyword) browser-id
```

```
@usage
(env->item-links {...} :my-browser)
```

```
@return (maps in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [env->item-links]]))

(engines.item-browser.api/env->item-links ...)
(env->item-links                          ...)
```

</details>

---

### env->pipeline-props

```
@param (map) env
@param (keyword) browser-id
```

```
@example
(env->pipeline-props {...} :my-browser)
=>
{:max-count 20
 :skip       0
 :filter-pattern {:$or [{:my-type/my-key "..."} {...}]}
 :search-pattern {:$or [{:my-type/name   "..."} {...}]}
 :sort-pattern   {:my-type/name 1}}
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [env->pipeline-props]]))

(engines.item-browser.api/env->pipeline-props ...)
(env->pipeline-props                          ...)
```

</details>

---

### env->search-pattern

```
@param (map) env
@param (keyword) browser-id
```

```
@usage
(env->search-pattern {...} :my-browser)
```

```
@return (map)
{:$or (maps in vector)}
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [env->search-pattern]]))

(engines.item-browser.api/env->search-pattern ...)
(env->search-pattern                          ...)
```

</details>

---

### env->sort-pattern

```
@param (map) env
@param (keyword) browser-id
```

```
@usage
(env->sort-pattern {...} :my-browser)
```

```
@return (map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [env->sort-pattern]]))

(engines.item-browser.api/env->sort-pattern ...)
(env->sort-pattern                          ...)
```

</details>

---

### item->parent-id

```
@param (map) env
@param (keyword) browser-id
@param (namespaced map) item
```

```
@example
(item->parent-id :my-browser {...})
=>
"parent-item"
```

```
@return (string)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item->parent-id]]))

(engines.item-browser.api/item->parent-id ...)
(item->parent-id                          ...)
```

</details>

---

### item->parent-link

```
@param (map) env
@param (keyword) browser-id
@param (namespaced map) item
```

```
@example
(item->parent-link :my-browser {...})
=>
{:my-type/id "parent-item"}
```

```
@return (namespaced map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item->parent-link]]))

(engines.item-browser.api/item->parent-link ...)
(item->parent-link                          ...)
```

</details>

---

### item->path

```
@param (map) env
@param (keyword) browser-id
@param (namespaced map) item
```

```
@example
(item->path :my-browser {...})
=>
[{:my-type/id "parent-item"}]
```

```
@return (maps in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item->path]]))

(engines.item-browser.api/item->path ...)
(item->path                          ...)
```

</details>

---

### item-id->parent-id

```
@param (map) env
@param (keyword) browser-id
@param (string) item-id
```

```
@example
(item-id->parent-id :my-browser "my-item")
=>
"parent-item"
```

```
@return (string)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item-id->parent-id]]))

(engines.item-browser.api/item-id->parent-id ...)
(item-id->parent-id                          ...)
```

</details>

---

### item-id->parent-link

```
@param (map) env
@param (keyword) browser-id
@param (string) item-id
```

```
@example
(item-id->parent-link :my-browser "my-item")
=>
{:my-type/id "parent-item"}
```

```
@return (namespaced map)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item-id->parent-link]]))

(engines.item-browser.api/item-id->parent-link ...)
(item-id->parent-link                          ...)
```

</details>

---

### item-id->path

```
@param (map) env
@param (keyword) browser-id
@param (string) item-id
```

```
@example
(item-id->path :my-browser "my-item")
=>
[{:my-type/id "parent-item"}]
```

```
@return (namespaced maps in vector)
```

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [engines.item-browser.api :refer [item-id->path]]))

(engines.item-browser.api/item-id->path ...)
(item-id->path                          ...)
```

</details>

---

This documentation is generated by the [docs-api](https://github.com/bithandshake/docs-api) engine

