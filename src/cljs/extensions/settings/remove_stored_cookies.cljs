
(ns extensions.settings.remove-stored-cookies
    (:require [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]
              [x.app-elements.api   :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/button ::cancel-button
                   {:color    :default
                    :label    :cancel!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click [:x.app-ui/close-popup! header-id]}])

(defn- remove-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/button ::remove-button
                   {:color    :warning
                    :label    :remove!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click {:dispatch-n [[:x.app-ui/close-popup! header-id]
                                            [::remove-stored-cookies!]]}}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [header-id]
  [elements/polarity ::header
                     {:start-content [cancel-button header-id]
                      :end-content   [remove-button header-id]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:<> [elements/separator {:orientation :horizontal :size :xs}]
       [elements/text      {:content :remove-stored-cookies? :font-weight :bold
                            :horizontal-align :center :icon :delete}]])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::remove-stored-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [message (r dictionary/look-up db :just-a-moment)]
           {:dispatch-later [{:ms   0 :dispatch [:x.app-environment.cookie-handler/remove-browser-cookies!]}
                             {:ms   0 :dispatch [:x.app-ui/set-shield! {:content message}]}
                             {:ms 500 :dispatch [:x.boot-loader/refresh-app!]}]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings/render-remove-stored-cookies-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup! ::view
                        {:content   #'body
                         :label-bar {:content #'header}
                         :layout    :boxed}])
