
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.events
    (:require [app-plugins.view-selector.engine :as engine]
              [app-plugins.view-selector.subs   :as subs]
              [x.app-core.api                   :as a :refer [r]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-current-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [extension-id :view-selector/meta-items :view-id] view-id))

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [derived-view-id (r subs/get-derived-view-id db extension-id)]
       (r set-current-view-id! db extension-id derived-view-id)))

(defn load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id selector-props]]
  (r store-derived-view-id! db extension-id))

(defn change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! db :my-extension :my-view)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (r set-current-view-id! db extension-id view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:view-selector/change-view! :my-extension :my-view]
(a/reg-event-db :view-selector/change-view! change-view!)
