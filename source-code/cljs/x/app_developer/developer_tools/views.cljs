
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.views
    (:require [layouts.popup-a.api                     :as popup-a]
              [x.app-core.api                          :as a]
              [x.app-developer.developer-tools.helpers :as developer-tools.helpers]
              [x.app-developer.event-browser.views     :rename {body event-browser}]
              [x.app-developer.re-frame-browser.views  :rename {body re-frame-browser}]
              [x.app-developer.request-inspector.views :rename {body request-inspector}]
              [x.app-developer.route-browser.views     :rename {body route-browser}]
              [x.app-elements.api                      :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-print-events-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [print-events? @(a/subscribe [:developer-tools/print-events?])]
       [elements/button ::toggle-event-print-button
                        {:hover-color :highlight
                         :label       "Print events"
                         :font-size   :xs
                         :indent      {:horizontal :xxs}
                         :on-click    [:core/set-debug-mode! (if print-events? "avocado-juice" "pineapple-juice")]
                         :preset      (if print-events? :primary :muted)}]))

(defn close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/icon-button ::close-icon-button
                        {:on-click [:ui/close-popup! :developer.developer-tools/view]
                         :preset   :close}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-polarity ::header
                                {:start-content [elements/menu-bar {:menu-items (developer-tools.helpers/menu-items)}]
                                 :end-content   [:<> [toggle-print-events-button]
                                                     [close-icon-button]]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:gestures/get-current-view-id :developer.developer-tools/handler])]
       (case view-id :re-frame-browser  [re-frame-browser]
                     :request-inspector [request-inspector]
                     :route-browser     [route-browser]
                     :event-browser     [event-browser])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [popup-id]
  [popup-a/layout popup-id {:body                #'body
                            :header              #'header
                            :min-width           :m
                            :stretch-orientation :vertical}])
