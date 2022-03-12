
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.views
    (:require [x.app-core.api                          :as a]
              [x.app-developer.re-frame-browser.views  :rename {body re-frame-browser}]
              [x.app-developer.request-inspector.views :rename {body request-inspector}]
              [x.app-developer.route-browser.views     :rename {body route-browser}]
              [x.app-elements.api                      :as elements]
              [x.app-ui.api                            :as ui]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:gestures/get-selected-view-id :developer.developer-tools/handler])]
       [{:label    "DB"
         :on-click [:gestures/change-view! :developer.developer-tools/handler :re-frame-browser]
         :active?  (= view-id :re-frame-browser)}
        {:label    "Requests"
         :on-click [:gestures/change-view! :developer.developer-tools/handler :request-inspector]
         :active?  (= view-id :request-inspector)}
        {:label    "Routes"
         :on-click [:gestures/change-view! :developer.developer-tools/handler :route-browser]
         :active?  (= view-id :route-browser)}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-print-events-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [print-events? @(a/subscribe [:developer-tools/print-events?])]
       [elements/button ::toggle-event-print-button
                        {:preset (if print-events? :primary-icon-button :muted-icon-button)
                         :icon :terminal :tooltip "Print events"
                         :on-click [:core/set-debug-mode! (if print-events? "avocado-juice" "pineapple-juice")]}]))

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-polarity {:start-content [elements/menu-bar {:menu-items (menu-items)}]
                                 :end-content   [:<> [toggle-print-events-button]
                                                     [ui/popup-close-icon-button :developer.developer-tools/view]]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [view-id @(a/subscribe [:gestures/get-selected-view-id :developer.developer-tools/handler])]
       (case view-id :re-frame-browser  [re-frame-browser]
                     :request-inspector [request-inspector]
                     :route-browser     [route-browser])))