
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.4.4
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.events
    (:require [mid-fruits.candy  :refer [param]]
              [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-lister.events    :as events]
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



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.events
(def store-lister-props! events/store-lister-props!)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  ;  {:download-limit (integer)
  ;   :label (metamorphic-content)
  ;   :order-by (keyword)
  ;   :order-by-options (keywords in vector)}
  [extension-id _ lister-props]
  (merge {:download-limit   DEFAULT-DOWNLOAD-LIMIT
          :label            extension-id
          :order-by         DEFAULT-ORDER-BY
          :order-by-options DEFAULT-ORDER-BY-OPTIONS
          :search-keys      DEFAULT-SEARCH-KEYS}
         (param lister-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (r store-lister-props! db extension-id item-namespace lister-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  [_ [_ extension-id item-namespace lister-props]]
  [:router/add-route! (engine/route-id extension-id item-namespace)
                      {:route-template (engine/route-template        extension-id)
                       :client-event   [:item-lister/load-lister!    extension-id item-namespace lister-props]
                       :on-leave-event [:item-lister/->lister-leaved extension-id item-namespace]
                       :restricted?    true}])

(a/reg-event-fx
  :item-lister/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;  {:download-limit (integer)(opt)
  ;    Default: DEFAULT-DOWNLOAD-LIMIT
  ;   :label (metamorphic-content)(opt)
  ;    Default: extension-id
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
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace lister-props]]
      (let [lister-props (lister-props-prototype extension-id item-namespace lister-props)]
           {:db       (r initialize!  db extension-id item-namespace lister-props)
            :dispatch (r add-route! cofx extension-id item-namespace lister-props)})))
