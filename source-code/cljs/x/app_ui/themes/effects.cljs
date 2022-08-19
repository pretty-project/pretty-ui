
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes.effects
    (:require [x.app-core.api         :as a :refer [r]]
              [x.app-ui.themes.events :as themes.events]
              [x.app-ui.themes.subs   :as themes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/change-theme!
  ; @param (keyword) theme-id
  ;
  ; @usage
  ;  [:ui/change-theme! :light]
  (fn [{:keys [db]} [_ theme-id]]
      {:db       (r themes.events/store-selected-theme! db theme-id)
       :dispatch [:ui/theme-changed]}))

(a/reg-event-fx
  :ui/theme-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [theme-id (r themes.subs/get-selected-theme db)]
           {:fx [:environment/set-element-attribute! "x-body-container" "data-theme" (name theme-id)]})))
