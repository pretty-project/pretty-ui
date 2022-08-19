
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification.lifecycles
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-browser-offline [:views/blow-no-internet-bubble?!]
   :on-app-launch      [:views/blow-no-internet-bubble?!]
   :on-browser-online  [:ui/close-bubble! :views.no-internet-notification/notification]})
