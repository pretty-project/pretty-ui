
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.error-page.effects
    (:require [x.app-core.api                :as a]
              [x.app-views.error-page.engine :as error-page.engine]
              [x.app-views.error-page.views  :as error-page.views]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-error-page!
  ; @param (keyword) error-id
  ;  :no-connection, :no-permission, :page-not-found, :under-construction, :under-maintenance
  ;
  ; @usage
  ;  [:views/render-error-page!]
  (fn [_ [_ error-id]]
      (let [content-props (get error-page.engine/ERROR-CONTENT error-id)]
           {:dispatch-n [[:ui/restore-default-title!]
                         [:ui/set-surface! :views.error-page/view
                                           {:view [error-page.views/view :views.error-page/view content-props]}]]})))
