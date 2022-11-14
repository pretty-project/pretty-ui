
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.title.effects
    (:require [re-frame.api    :as r :refer [r]]
              [x.core.api      :as x.core]
              [x.ui.title.subs :as title.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.ui/restore-default-window-title!
  ; @usage
  ;  [:x.ui/restore-default-window-title!]
  (fn [{:keys [db]} _]
      (let [tab-title (r x.core/get-app-config-item db :app-title)]
           {:fx [:x.environment/set-tab-title! tab-title]})))

(r/reg-event-fx :x.ui/set-window-title!
  ; @param (metamorphic-content) tab-title
  ;
  ; @usage
  ;  [:x.ui/set-window-title! "My title"]
  (fn [{:keys [db]} [_ tab-title]]
      (let [tab-title (r title.subs/get-tab-title-value db tab-title)]
           {:fx [:x.environment/set-tab-title! tab-title]})))
