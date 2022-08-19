
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.title.effects
    (:require [x.app-core.api      :as a :refer [r]]
              [x.app-ui.title.subs :as title.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/restore-default-window-title!
  ; @usage
  ;  [:ui/restore-default-window-title!]
  (fn [{:keys [db]} _]
      (let [window-title (r a/get-app-config-item db :app-title)]
           {:fx [:environment/set-window-title! window-title]})))

(a/reg-event-fx
  :ui/set-window-title!
  ; @param (metamorphic-content) window-title
  ;
  ; @usage
  ;  [:ui/set-window-title! "My title"]
  (fn [{:keys [db]} [_ window-title]]
      (let [window-title (r title.subs/get-window-title-value db window-title)]
           {:fx [:environment/set-window-title! window-title]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/restore-default-title!
  ; @usage
  ;  [:ui/restore-default-title!]
  (fn [{:keys [db]} _]
      (let [window-title (r a/get-app-config-item db :app-title)]
           {:fx-n [[:environment/set-window-title! window-title]
                   [:ui/remove-header-title!]]})))

(a/reg-event-fx
  :ui/set-title!
  ; @param (metamorphic-content) title
  ;
  ; @usage
  ;  [:ui/set-title! "My title"]
  (fn [{:keys [db]} [_ title]]
      (let [window-title (r title.subs/get-window-title-value db title)]
           {:fx-n [[:environment/set-window-title! window-title]
                   [:ui/set-header-title!                 title]]})))
