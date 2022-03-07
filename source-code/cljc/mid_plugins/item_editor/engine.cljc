
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

(defn add-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (engine/add-item-label :my-extension :my-type)
  ;  =>
  ;  :add-my-type
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (keyword (str "add-" (name item-namespace))))

(defn edit-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @example
  ;  (engine/edit-item-label :my-extension :my-type)
  ;  =>
  ;  :edit-my-type
  ;
  ; @return (metamorphic-content)
  [extension-id item-namespace item-id]
  (keyword (str "edit-" (name item-namespace))))

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
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/mutation-name :my-extension :my-type :add)
  ;  =>
  ;  "my-extension.my-type-editor/add-item!"
  ;
  ; @return (string)
  [extension-id item-namespace action-key]
  (str (name extension-id)   "."
       (name item-namespace) "-editor/"
       (name action-key)     "-item!"))

(defn resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/resolver-id :my-extension :my-type :get)
  ;  =>
  ;  :my-extension.my-type-editor/get-item
  ;
  ; @return (keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           (str (name action-key)     "-item")))

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
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           "route"))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:base-route (string)}
  ;
  ; @example
  ;  (engine/extended-route-template :my-extension :my-type {:base-route "/@app-home/my-extension"})
  ;  =>
  ;  "/@app-home/my-extension/:item-id"
  ;
  ; @return (string)
  [_ _ {:keys [base-route]}]
  (str base-route "/:item-id"))

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
  ; @param (keyword) action-key
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
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)  "."
                (name item-namespace) "-editor")
           (str (name action-key)     "-dialog")))
