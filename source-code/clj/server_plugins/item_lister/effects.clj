
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.effects
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a :refer [r]]
              [server-plugins.item-lister.events :as events]
              [server-plugins.item-lister.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced keywords in vector)
(def DEFAULT-ORDER-BY-OPTIONS [:modified-at/descending :modified-at/ascending :name/ascending :name/descending])

; @constant (integer)
(def DEFAULT-DOWNLOAD-LIMIT 20)

; @constant (keywords in vector)
(def DEFAULT-SEARCH-KEYS [:name])



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
  ;  {:collection-name (string)
  ;   :download-limit (integer)
  ;   :order-by-options (namespaced keywords in vector)
  ;   :route-title (metamorphic-content)
  ;   :routed? (boolean)
  ;   :search-keys (keywords in vector)}
  [extension-id _ lister-props]
  (merge {:download-limit   DEFAULT-DOWNLOAD-LIMIT
          :order-by-options DEFAULT-ORDER-BY-OPTIONS
          :search-keys      DEFAULT-SEARCH-KEYS
          :route-title      (param extension-id)
          :collection-name  (name  extension-id)
          :routed?          true}
         (param lister-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/init-lister!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;  {:collection-name (string)(opt)
  ;    Default: (name extension-id)
  ;   :download-limit (integer)(opt)
  ;    Default: DEFAULT-DOWNLOAD-LIMIT
  ;   :route-title (metamorphic-content)(opt)
  ;    Default: extension-id
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: DEFAULT-ORDER-BY-OPTIONS
  ;   :routed? (boolean)(opt)
  ;    Default: true
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: DEFAULT-SEARCH-KEYS}
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-extension :my-type]
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-extension :my-type {...}]
  ;
  ; @usage
  ;  [:item-lister/init-lister! :my-extension :my-type {:search-keys [:name :email-address]}]
  (fn [{:keys [db]} [_ extension-id item-namespace lister-props]]
      (let [lister-props (lister-props-prototype extension-id item-namespace lister-props)]
           {:db (r events/init-lister! db extension-id item-namespace lister-props)
            :dispatch-n [[:item-lister/reg-transfer-lister-props! extension-id item-namespace lister-props]
                         [:item-lister/add-route!                 extension-id item-namespace lister-props]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/reg-transfer-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  (fn [_ [_ extension-id item-namespace lister-props]]
      {:fx [:core/reg-transfer! (engine/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return lister-props))
                                 :target-path [extension-id :item-lister/meta-items]}]}))

(a/reg-event-fx
  :item-lister/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:routed? (boolean)}
  (fn [_ [_ extension-id item-namespace {:keys [routed?]}]]
      (if routed? [:router/add-route! (engine/route-id extension-id item-namespace)
                                      {:route-template (engine/route-template     extension-id item-namespace)
                                       :client-event   [:item-lister/load-lister! extension-id item-namespace]
                                       :restricted?    true}])))
