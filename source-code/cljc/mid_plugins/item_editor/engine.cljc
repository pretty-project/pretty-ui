
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.engine
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;   Pl.: :my-extension.item-editor/synchronize-editor!
;        =>
;        :my-extension.my-type-editor/synchronize-editor!
;   Így biztosítható, hogy egy névtér több különböző item-editor szerkesztőt tudjon megvalósítani.
; - Ha szükséges, akkor a Re-Frame adatbázis útvonalakban is be kell vezetni a megkülönbözetést,
;   hogy egy extension több szerkesztőt alkalmazhasson.



;; -- Public helpers ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-uri
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (item-editor/editor-uri :my-extension :my-type "my-item")
  ;  =>
  ;  "/@app-home/my-extension/my-item"
  ;
  ; @return (string)
  [extension-id _ item-id]
  (str "/@app-home/" (name  extension-id)
       "/"           (param item-id)))

(defn form-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/form-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-editor/form
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           "form"))

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-editor/synchronize-editor!
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           "synchronize-editor!"))



;; -- Private helpers ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-item-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (engine/data-item-path :my-extension :my-type :my-value)
  ;  =>
  ;  [:my-extension :my-type-editor/data-items :my-value]
  ;
  ; @return (item-path vector)
  [extension-id item-namespace & xyz]
  (let [data-items-key (keyword (str (name item-namespace) "-editor/data-items"))]
       (vector/concat-items [extension-id data-items-key] xyz)))

(defn meta-item-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (list of *) xyz
  ;
  ; @example
  ;  (engine/meta-item-path :my-extension :my-type :my-value)
  ;  =>
  ;  [:my-extension :my-type-editor/meta-items :my-value]
  ;
  ; @return (item-path vector)
  [extension-id item-namespace & xyz]
  (let [meta-items-key (keyword (str (name item-namespace) "-editor/meta-items"))]
       (vector/concat-items [extension-id meta-items-key] xyz)))

(defn item-id->new-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (engine/item-id->new-item? :my-extension :my-type "new-my-type")
  ;  =>
  ; true
  ;
  ; @example
  ;  (engine/item-id->new-item? :my-extension :my-type "my-item")
  ;  =>
  ; false
  ;
  ; @return (boolean)
  [_ item-namespace item-id]
  (= item-id (str "new-" (name item-namespace))))

(defn item-id->editor-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (engine/item-id->editor-title :my-extension :my-type "new-my-type")
  ;  =>
  ; :add-my-type
  ;
  ; @example
  ;  (engine/item-id->editor-title :my-extension :my-type "my-item")
  ;  =>
  ; :edit-my-type
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (if (item-id->new-item? extension-id item-namespace item-id)
      (keyword/join "add-"  item-namespace)
      (keyword/join "edit-" item-namespace)))

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/new-item-label :my-extension :my-type)
  ;  =>
  ;  :new-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "new-" (name item-namespace))))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/new-item-label :my-extension :my-type)
  ;  =>
  ;  :unnamed-my-type
  ;
  ; @return (metamorphic-content)
  [_ item-namespace]
  (keyword (str "unnamed-" (name item-namespace))))

(defn mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (engine/mutation-name :my-extension :my-type :add)
  ;  =>
  ;  "my-extension.my-type-editor/add-item!"
  ;
  ; @return (string)
  [extension-id item-namespace action-id]
  (str (name extension-id)   "."
       (name item-namespace) "-editor/"
       (name action-id)      "-item!"))

(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type :get)
  ;  =>
  ;  :my-extension.my-type-editor/get-item
  ;
  ; @return (keyword)
  [extension-id item-namespace action-id]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           (str (name action-id)      "-item")))

(defn collection-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/collection-name :my-extension :my-type)
  ;  =>
  ;  "my-extension"
  ;
  ; @return (string)
  [extension-id _]
  (name extension-id))

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/transfer-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-editor/transfer-editor-props
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           "transfer-editor-props"))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-editor/route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)  "."
                (name item-namespace) "-editor")
           "route"))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/route-template :my-extension :my-type)
  ;  =>
  ;  "/@app-home/my-extension/:item-id"
  ;
  ; @return (string)
  [extension-id _]
  (str "/@app-home/" (name extension-id)
       "/:item-id"))

(defn parent-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/parent-uri :my-extension :my-type)
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (string)
  [extension-id _]
  (str "/@app-home/" (name extension-id)))

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (engine/component-id :my-extension :my-type :view)
  ;  =>
  ;  :my-extension.my-type-editor/view
  ;
  ; @return (keyword)
  [extension-id item-namespace component-key]
  ; XXX#5467
  ; A [components/stated ...] komponens {:initializer [...]} és {:destructor [...]} eseményei
  ; egyedi azonosító hiányában a komponens újrarenderelésekor ismételten megtörténnének!
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           (name component-key)))

(defn dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-id
  ;
  ; @example
  ;  (engine/dialog-id :my-extension :my-type :color-picker)
  ;  =>
  ;  :my-extension.my-type-editor/color-picker-dialog
  ;
  ; @example
  ;  (engine/dialog-id :my-extension :my-type :item-deleted)
  ;  =>
  ;  :my-extension.my-type-editor/item-deleted-dialog
  ;
  ; @return (namespaced keyword)
  [extension-id item-namespace action-id]
  (keyword (str (name extension-id)  "."
                (name item-namespace) "-editor")
           (str (name action-id)      "-dialog")))

(defn load-extension-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/load-extension-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension.my-type-editor/load-editor!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-editor")
                          "load-editor!")]
       [event-id]))
