
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.core.events
    (:require [plugins.view-selector.core.subs :as core.subs]
              [x.app-core.api                  :as a :refer [r]]
              [x.app-db.api                    :as db]
              [x.app-ui.api                    :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn change-view!
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r view-selector/change-view! :my-extension :my-view)
  ;
  ; @return (map)
  [db [_ extension-id view-id]]
  (assoc-in db [extension-id :view-selector/meta-items :view-id] view-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [derived-view-id (r core.subs/get-derived-view-id db extension-id)]
       (assoc-in db [extension-id :view-selector/meta-items :view-id] derived-view-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn load-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (if-let [route-title (r core.subs/get-meta-item db extension-id :route-title)]
          (as-> db % (r store-derived-view-id! % extension-id)
                     (r ui/set-header-title!   % route-title))
          (r store-derived-view-id! db extension-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) view-props
  ;
  ; @return (map)
  [db [_ extension-id view-props]]
  (as-> db % (r db/apply-item! % [extension-id :view-selector/meta-items] merge view-props)
             (assoc-in % [extension-id :view-selector/meta-items :body-mounted?] true)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-did-mount body-did-mount)
