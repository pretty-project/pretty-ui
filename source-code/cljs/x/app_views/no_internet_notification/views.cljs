
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.no-internet-notification.views
    (:require [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  [_]
  [elements/horizontal-polarity ::body
                                {:start-content [elements/label  {:content  :no-internet-connection}]
                                 :end-content   [elements/button {:label    :refresh!
                                                                  :on-click [:boot-loader/refresh-app!]
                                                                  :preset   :primary-button}]}])
