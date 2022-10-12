
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.event-browser.views
    (:require [mid-fruits.vector  :as vector]
              [re-frame.api       :as a]
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
                                        {:on-click [:db/remove-item! [:developer :event-browser/meta-items]]
                                         :preset   :back}]
                  [elements/label ::event-id
                                  {:color       :muted
                                   :content     (str event-id)
                                   :font-size   :m
                                   :font-weight :extra-bold}]
                  [elements/icon-button {:variant :placeholder}]]
            [:div {:style {:padding "0 48px"}}
                  [elements/label ::event-vector
                                  {:color   :muted
                                   :content (str "["event-id (if parameters (str " " parameters)) "]")}]
                  [elements/text-field ::event-vector
                                       {:indent     {:bottom :xxl}
                                        :label      "Parameters"
                                        :value-path [:developer :event-browser/meta-items :parameters]}]
                  [elements/button ::dispatch-button
                                   {:background-color :primary
                                    :border-radius    :l
                                    :label            "Dispatch"
                                    :on-click         [:developer/dispatch-current-event!]
                                    :style            {:width "240px"}}]]]))

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
                                    [elements/label {:color       :muted
                                                     :content     "Registrated events"
                                                     :font-size   :m
                                                     :font-weight :extra-bold}]]]
                        (-> event-handlers keys vector/abc-items)))))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [current-event @(a/subscribe [:db/get-item [:developer :event-browser/meta-items :current-event]])]
          [event-view]
          [event-list]))
