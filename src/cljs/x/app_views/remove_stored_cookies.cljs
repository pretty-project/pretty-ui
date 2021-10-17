
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.31
; Description:
; Version: v0.2.0
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.remove-stored-cookies
    (:require [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]
              [x.app-elements.api   :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- remove-stored-cookies-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/button {:color    :default
                    :label    :cancel!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click [:x.app-ui/close-popup! popup-id]}])

(defn- remove-stored-cookies-remove-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/button {:color    :warning
                    :label    :remove!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click {:dispatch-n [[:x.app-ui/close-popup! popup-id]
                                            [::remove-stored-cookies!]]}}])

(defn- remove-stored-cookies-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [popup-id]
  [elements/polarity {:start-content [remove-stored-cookies-cancel-button popup-id]
                      :end-content   [remove-stored-cookies-remove-button popup-id]}])

(defn- remove-stored-cookies-dialog
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [_]
  [:<> [elements/separator {:orientation :horizontal :size :xs}]
       [elements/text {:content :remove-stored-cookies? :font-weight :bold
                       :horizontal-align :center :icon :delete}]])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::remove-stored-cookies!
  (fn [{:keys [db]} _]
      (let [message (r dictionary/look-up db :just-a-moment)]
           {:dispatch-later
            [{:ms   0 :dispatch [:x.app-environment.cookie-handler/remove-browser-cookies!]}
             {:ms   0 :dispatch [:x.app-ui/set-shield! {:content message}]}
             {:ms 500 :dispatch [:x.boot-loader/refresh-app!]}]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/add-popup!
   ::remove-stored-cookies?
   {:content   #'remove-stored-cookies-dialog
    :label-bar {:content #'remove-stored-cookies-label-bar}
    :layout    :boxed}])
