
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.effects
    (:require [x.app-core.api                        :as a :refer [r]]
              [x.app-developer.developer-tools.views :as developer-tools.views]
              [x.app-gestures.api                    :as gestures]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/test!
  ; @usage
  ;  [:developer/test!]
  [:ui/render-bubble! {:body "It works!"}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/render-developer-tools!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r gestures/init-view-handler! db :developer.developer-tools/handler
                                                   {:default-view-id :re-frame-browser :reinit? false})
       :dispatch [:ui/render-popup! :developer.developer-tools/view
                                    {:body   #'developer-tools.views/body
                                     :header #'developer-tools.views/header
                                     :stretch-orientation :vertical}]}))
