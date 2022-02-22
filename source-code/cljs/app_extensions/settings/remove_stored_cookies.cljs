
(ns app-extensions.settings.remove-stored-cookies
    (:require [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]
              [x.app-elements.api   :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::cancel-button
                   {:color    :default
                    :label    :cancel!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click [:ui/close-popup! :settings.remove-stored-cookies/view]}])

(defn- remove-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/button ::remove-button
                   {:color    :warning
                    :label    :remove!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click {:dispatch-n [[:ui/close-popup! :settings.remove-stored-cookies/view]
                                            [:settings/remove-stored-cookies!]]}}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/horizontal-polarity ::header
                                {:start-content [cancel-button]
                                 :end-content   [remove-button]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:<> [elements/horizontal-separator {:size :xs}]
       [elements/text {:content :remove-stored-cookies? :font-weight :bold
                       :horizontal-align :center :icon :delete}]])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings/remove-stored-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [message (r dictionary/look-up db :just-a-moment)]
           {:dispatch-later [{:ms   0 :dispatch [:ui/set-shield! {:content message}]}
                             {:ms  50 :dispatch [:environment/remove-cookies!]}
                             {:ms 500 :dispatch [:boot-loader/refresh-app!]}]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.remove-stored-cookies/render-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! :settings.remove-stored-cookies/view
                  {:body   #'body
                   :header #'header}])
