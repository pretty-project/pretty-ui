
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.engine
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.string  :as string]
              [mid-fruits.uri     :as uri]
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

(defn value-path
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) item-key
  ;
  ; @example
  ;  (item-editor/value-path :my-extension :my-type :my-item)
  ;  =>
  ;  [:my-extension :item-editor/data-items :my-item]
  ;
  ; @return (item-path vector)
  [extension-id _ item-key]
  [extension-id :item-editor/data-items item-key])

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
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (engine/route-template :my-extension :my-type {:route-template "/@app-home/my-extension/:item-id"})
  ;  =>
  ;  "/@app-home/my-extension/:item-id"
  ;
  ; @return (keyword)
  [extension-id item-namespace {:keys [route-template]}]
  (uri/valid-path route-template))

(defn base-route
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;  {:route-template (string)}
  ;
  ; @example
  ;  (engine/base-route :my-extension :my-type {:route-template "/@app-home/my-extension/:item-id"})
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (keyword)
  [extension-id item-namespace {:keys [route-template]}]
  (-> route-template (string/not-ends-with! "/:item-id")
                     (uri/valid-path)))

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
