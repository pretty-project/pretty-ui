
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.events
    (:require [mid-fruits.candy  :refer [param]]
              [x.server-core.api :as a :refer [r]]
              [server-plugins.item-lister.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def DEFAULT-ORDER-BY-OPTIONS [:by-date-descending :by-date-ascending :by-name-descending :by-name-ascending])

; @constant (keyword)
(def DEFAULT-ORDER-BY :by-date-descending)

; @constant (integer)
(def DEFAULT-DOWNLOAD-LIMIT 20)

; @constant (keywords in vector)
(def DEFAULT-SEARCH-KEYS [:name])



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- lister-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) lister-props
  ;
  ; @return (map)
  ;  {:download-limit (integer)
  ;   :handle-archived-items? (boolean)
  ;   :handle-favorite-items? (boolean)
  ;   :order-by (keyword)
  ;   :order-by-options (keywords in vector)}
  [lister-props]
  (merge {:download-limit   DEFAULT-DOWNLOAD-LIMIT
          :handle-archived-items? true
          :handle-favorite-items? true
          :order-by         DEFAULT-ORDER-BY
          :order-by-options DEFAULT-ORDER-BY-OPTIONS
          :search-keys      DEFAULT-SEARCH-KEYS}
         (param lister-props)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  [_ [_ extension-id item-namespace lister-props]]
  [:router/add-route! (engine/route-id extension-id item-namespace)
                      {:route-template (engine/route-template     extension-id item-namespace)
                       :client-event   [:item-lister/load-lister! extension-id item-namespace lister-props]
                       :restricted?    true}])

(a/reg-event-fx
  :item-lister/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;  {:download-limit (integer)(opt)
  ;    Default: DEFAULT-DOWNLOAD-LIMIT
  ;   :handle-archived-items? (boolean)(opt)
  ;    Default: true
  ;   :handle-favorite-items? (boolean)(opt)
  ;    Default: true
  ;   :order-by (keyword)(opt)
  ;    Default: DEFAULT-ORDER-BY
  ;   :order-by-options (keywords in vector)(opt)
  ;    Default: DEFAULT-ORDER-BY-OPTIONS
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: DEFAULT-SEARCH-KEYS}
  ;
  ; @usage
  ;  [:item-lister/initialize! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-lister/initialize! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-lister/initialize! :my-extension :my-type {:search-keys [:name :email-address]}]
  (fn [cofx [_ extension-id item-namespace lister-props]]
      (let [lister-props (a/prot lister-props lister-props-prototype)]
           (r add-route! cofx extension-id item-namespace lister-props))))
