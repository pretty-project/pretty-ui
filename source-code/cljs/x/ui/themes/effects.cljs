
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.themes.effects
    (:require [re-frame.api       :as r :refer [r]]
              [x.ui.themes.events :as themes.events]
              [x.ui.themes.subs   :as themes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.ui/change-theme!
  ; @param (keyword) theme-id
  ;
  ; @usage
  ;  [:x.ui/change-theme! :light]
  (fn [{:keys [db]} [_ theme-id]]
      {:db       (r themes.events/store-selected-theme! db theme-id)
       :dispatch [:x.ui/theme-changed]}))

(r/reg-event-fx :x.ui/theme-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [theme-id (r themes.subs/get-selected-theme db)]
           {:fx [:x.environment/set-element-attribute! "x-body-container" "data-theme" (name theme-id)]})))
