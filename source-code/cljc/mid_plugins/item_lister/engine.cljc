
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-lister.engine
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;   Pl.: :my-extension.item-lister/synchronize-lister!
;        =>
;        :my-extension.my-type-lister/synchronize-lister!
;   Így biztosítható, hogy egy névtér több különböző item-lister listázót tudjon megvalósítani.
; - Ha szükséges, akkor a Re-Frame adatbázis útvonalakban is be kell vezetni a megkülönbözetést,
;   hogy egy extension több listázót alkalmazhasson.



;; -- Public helpers ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (item-lister/request-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-lister/synchronize-lister!
  ;
  ; @return (keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "synchronize-lister!"))



;; -- Private helpers ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/mutation-name :my-extension :my-type :delete)
  ;  =>
  ;  "my-extension.my-type-lister/delete-items!"
  ;
  ; @return (string)
  [extension-id item-namespace action-key]
  (str (name extension-id)   "."
       (name item-namespace) "-lister/"
       (name action-key)     "-items!"))

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
  ;  :my-extension.my-type-lister/get-items
  ;
  ; @return (keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           (str (name action-key)     "-items")))

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
  ;  :my-extension.my-type-lister/transfer-lister-props
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "transfer-lister-props"))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/route-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-lister/route
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           "route"))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:base-route (string)}
  ;
  ; @example
  ;  (engine/route-template :my-extension :my-type {:base-route "/@app-home/my-extension"})
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (string)
  [_ _ {:keys [base-route]}]
  (return base-route))

(defn add-new-item-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (engine/add-new-item-event :my-extension :my-type)
  ;  =>
  ;  [:my-extension.my-type-lister/add-new-item!]
  ;
  ; @return (event-vector)
  [extension-id item-namespace]
  (let [event-id (keyword (str (name extension-id)   "."
                               (name item-namespace) "-lister")
                          "add-new-item!")]
       [event-id]))

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
  ;  :my-extension.my-type-lister/view
  ;
  ; @return (keyword)
  [extension-id item-namespace component-key]
  ; XXX#5467
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           (name component-key)))

(defn dialog-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (engine/dialog-id :my-extension :my-type :delete-items)
  ;  =>
  ;  :my-extension.my-type-lister/delete-items-dialog
  ;
  ; @return (namespaced keyword)
  [extension-id item-namespace action-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-lister")
           (str (name action-key)     "-dialog")))
