
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.event-browser.views
    (:require [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [event-id   @(a/subscribe [:db/get-item [:developer :event-browser/meta-items :current-event]])
        parameters @(a/subscribe [:db/get-item [:developer :event-browser/meta-items :parameters]])]
       [:div {:style {:width "100%"}}
            [:div {:style {:display :flex}}
                  [elements/icon-button ::back-button
                                        {:preset :back :variant :transparent :color :default
                                         :on-click [:db/remove-item! [:developer :event-browser/meta-items]]}]
                  [elements/label ::event-id
                                  {:content (str event-id) :font-size :m :font-weight :extra-bold :color :muted}]
                  [elements/icon-button {:variant :placeholder}]]
            [:div {:style {:padding "0 48px"}}
                  [elements/label ::event-vector
                                  {:content (str "["event-id (if parameters (str " " parameters)) "]")
                                   :color :muted}]
                  [elements/text-field ::event-vector
                                       {:label "Parameters"
                                        :value-path [:developer :event-browser/meta-items :parameters]}]
                  [elements/horizontal-separator {:size :xxl}]
                  [elements/button ::dispatch-button
                                   {:label "Dispatch" :on-click [:developer/dispatch-current-event!]
                                    :style {:width "240px"}}]]]))

(defn event-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [event-id event-props]
  [:button {:style {:display :block :text-align :left :padding "0px 48px" :width "100%"}
            :on-click #(a/dispatch [:db/set-item! [:developer :event-browser/meta-items :current-event] event-id])}
           [:div {:style {:font-size "14px" :font-weight "500"}}
                 (str "[" event-id " ...]")]])

(defn event-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [event-handlers (x.app-core.event-handler/get-event-handlers :event)]
       (letfn [(f [event-list event-id]
                  (let [event-props (get event-handlers event-id)]
                       (conj event-list [event-list-item event-id event-props])))]
              (reduce f [:div {:style {:width "100%" :padding "0 0px"}}
                              [:div {:style {:display "flex"}}
                                    [elements/icon-button {:variant :placeholder}]
                                    [elements/label {:content "Registrated events" :font-size :m :color :muted :font-weight :extra-bold}]]]
                        (-> event-handlers keys vector/abc-items)))))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [current-event @(a/subscribe [:db/get-item [:developer :event-browser/meta-items :current-event]])]
          [event-view]
          [event-list]))
