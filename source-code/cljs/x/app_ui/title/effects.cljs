
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
      (let [window-title (r x.core/get-app-config-item db :app-title)]
           {:fx [:environment/set-window-title! window-title]})))

(r/reg-event-fx :ui/set-window-title!
  ; @param (metamorphic-content) window-title
  ;
  ; @usage
  ;  [:ui/set-window-title! "My title"]
  (fn [{:keys [db]} [_ window-title]]
      (let [window-title (r title.subs/get-window-title-value db window-title)]
           {:fx [:environment/set-window-title! window-title]})))
