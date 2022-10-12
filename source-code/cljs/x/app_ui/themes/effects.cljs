
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes.effects
    (:require [re-frame.api           :as r :refer [r]]
              [x.app-ui.themes.events :as themes.events]
              [x.app-ui.themes.subs   :as themes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui/change-theme!
  ; @param (keyword) theme-id
  ;
  ; @usage
  ;  [:ui/change-theme! :light]
  (fn [{:keys [db]} [_ theme-id]]
      {:db       (r themes.events/store-selected-theme! db theme-id)
       :dispatch [:ui/theme-changed]}))

(r/reg-event-fx :ui/theme-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [theme-id (r themes.subs/get-selected-theme db)]
           {:fx [:environment/set-element-attribute! "x-body-container" "data-theme" (name theme-id)]})))
