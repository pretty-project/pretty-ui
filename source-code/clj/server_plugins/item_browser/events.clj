
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-browser.events    :as events]
              [server-plugins.item-browser.engine :as engine]
              [server-plugins.item-lister.api     :as item-lister]
              [server-plugins.item-lister.events  :refer [lister-props-prototype]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-LABEL-KEY :name)

; @constant (keyword)
(def DEFAULT-PATH-KEY :path)



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.events
(def store-browser-props! events/store-browser-props!)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  ;  {:label-key (keyword)
  ;   :path-key (keyword)}
  [extension-id item-namespace browser-props]
  (merge {:label-key DEFAULT-LABEL-KEY
          :path-key  DEFAULT-PATH-KEY}
         (lister-props-prototype extension-id item-namespace browser-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (let [; XXX#0551
        lister-props  (map/dissoc-items browser-props engine/BROWSER-PROPS-KEYS)
        browser-props (select-keys      browser-props engine/BROWSER-PROPS-KEYS)]
       (as-> db % (r store-browser-props! % extension-id item-namespace browser-props)
                  (r server-plugins.item-lister.events/initialize! % extension-id item-namespace lister-props))))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  [_ [_ extension-id item-namespace browser-props]]
  [:router/add-route! (engine/route-id extension-id item-namespace)
                      {:route-template (engine/route-template       extension-id)
                       :client-event   [:item-browser/load-browser! extension-id item-namespace browser-props]
                       :restricted?    true}])

(defn add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  [_ [_ extension-id item-namespace browser-props]]
  [:router/add-route! (engine/extended-route-id extension-id item-namespace)
                      {:route-template (engine/extended-route-template extension-id)
                       :client-event   [:item-browser/load-browser!    extension-id item-namespace browser-props]
                       :restricted?    true}])

(a/reg-event-fx
  :item-browser/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:default-item-id (string)(opt)
  ;   :download-limit (integer)(opt)
  ;    Default: item-lister/DEFAULT-DOWNLOAD-LIMIT
  ;   :label (metamorphic-content)(opt)
  ;    Default: extension-id
  ;   :label-key (keyword)(opt)
  ;    Default: DEFAULT-LABEL-KEY
  ;   :order-by (keyword)(opt)
  ;    Default: item-lister/DEFAULT-ORDER-BY
  ;   :order-by-options (keywords in vector)(opt)
  ;    Default: item-lister/DEFAULT-ORDER-BY-OPTIONS
  ;   :path-key (keyword)(opt)
  ;    Default: DEFAULT-PATH-KEY
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: item-lister/DEFAULT-SEARCH-KEYS}
  ;
  ; @usage
  ;  [:item-browser/initialize! :my-extension :my-type {:default-item-id "my-item"
  ;                                                     :search-keys     [:name :email-address]}]
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace browser-props]]
      (let [browser-props (browser-props-prototype extension-id item-namespace browser-props)]
           {:db          (r initialize!           db extension-id item-namespace browser-props)
            :dispatch-n [(r add-route!          cofx extension-id item-namespace browser-props)
                         (r add-extended-route! cofx extension-id item-namespace browser-props)]})))
