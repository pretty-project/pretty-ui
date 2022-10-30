
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title.effects
    (:require [re-frame.api        :as r :refer [r]]
              [x.app-core.api      :as x.core]
              [x.app-ui.title.subs :as title.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui/restore-default-window-title!
  ; @usage
  ;  [:ui/restore-default-window-title!]
  (fn [{:keys [db]} _]
      (let [tab-title (r x.core/get-app-config-item db :app-title)]
           {:fx [:environment/set-tab-title! tab-title]})))

(r/reg-event-fx :ui/set-window-title!
  ; @param (metamorphic-content) tab-title
  ;
  ; @usage
  ;  [:ui/set-window-title! "My title"]
  (fn [{:keys [db]} [_ tab-title]]
      (let [tab-title (r title.subs/get-tab-title-value db tab-title)]
           {:fx [:environment/set-tab-title! tab-title]})))
