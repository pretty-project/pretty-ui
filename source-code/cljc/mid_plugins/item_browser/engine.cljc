
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-browser.engine
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.string  :as string]
              [mid-fruits.uri     :as uri]
              [mid-fruits.vector  :as vector]))



;; -- Description -------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; - Az elnevézesekben az item-namespace értéke helyettesíti az "item" szót.
;   Pl.: :my-extension.item-browser/synchronize-browser!
;        =>
;        :my-extension.my-type-browser/synchronize-browser!
;   Így biztosítható, hogy egy névtér több különböző item-browser böngészőt tudjon megvalósítani.
; - Ha szükséges, akkor a Re-Frame adatbázis útvonalakban is be kell vezetni a megkülönbözetést,
;   hogy egy extension több böngészőt alkalmazhasson.



;; -- Private helpers ---------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ;  :my-extension.my-type-browser/transfer-browser-props
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           "transfer-browser-props"))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) route-key
  ;
  ; @example
  ;  (engine/route-id :my-extension :my-type :extended)
  ;  =>
  ;  :my-extension.my-type-browser/extended-route
  ;
  ; @return (keyword)
  [extension-id item-namespace route-key]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           (str (name route-key)      "-route")))

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
  [_ _ {:keys [route-template]}]
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
  [_ _ {:keys [route-template]}]
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
  ;  :my-extension.my-type-browser/view
  ;
  ; @return (keyword)
  [extension-id item-namespace component-key]
  ; XXX#5467
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-browser")
           (name component-key)))

  ;(keyword (namespace browser-id)
  ;         (str (name browser-id) "-"
  ;              (name component-key))))
