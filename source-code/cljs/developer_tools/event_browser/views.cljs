
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.event-browser.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [event-id   @(r/subscribe [:x.db/get-item [:developer-tools :event-browser/meta-items :current-event]])
        parameters @(r/subscribe [:x.db/get-item [:developer-tools :event-browser/meta-items :parameters]])]
       [:div {:style {:width "100%"}}
            [:div {:style {:display :flex}}
                  [elements/icon-button ::back-button
                                        {:on-click [:x.db/remove-item! [:developer-tools :event-browser/meta-items]]
                                         :preset   :back}]
                  [elements/label ::event-id
                                  {:color       :muted
                                   :content     (str event-id)
                                   :font-size   :m
                                   :font-weight :extra-bold
                                   :line-height :block}]
                  [elements/icon-button {:variant :placeholder}]]
            [:div {:style {:padding "0 48px"}}
                  [elements/text-field ::event-vector
                                       {:indent     {:bottom :s}
                                        :label      "Parameters"
                                        :value-path [:developer-tools :event-browser/meta-items :parameters]}]
                  [elements/label ::event-vector
                                  {:color   :muted
                                   :content (str "["event-id (if parameters (str " " parameters)) "]")
                                   :font-size :xs
                                   :indent     {:bottom :s}
                                   :line-height :block}]
                  [elements/button ::dispatch-button
                                   {:background-color :highlight
                                    :border-radius    :xs
                                    :indent           {:bottom :m}
                                    :label            "Dispatch"
                                    :on-click         [:developer-tools.event-browser/dispatch-current-event!]
                                    :style            {:width "240px"}}]]]))

(defn event-list-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [event-id event-props]
  [:button {:style {:display :block :text-align :left :padding "0px 12px" :width "100%"}
            :on-click #(r/dispatch [:x.db/set-item! [:developer-tools :event-browser/meta-items :current-event] event-id])}
           [:div {:style {:font-size "14px" :font-weight "500"}}
                 (str "[" event-id " ...]")]])

(defn event-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [event-handlers (r/get-event-handlers :event)]
       (letfn [(f [event-list event-id]
                  (let [event-props (get event-handlers event-id)]
                       (conj event-list [event-list-item event-id event-props])))]
              (reduce f [:div {:style {:width "100%" :padding "0 0px"}}
                              [:div {:style {:display "flex"}}
                                    [elements/icon-button {:variant :placeholder}]
                                    [elements/label {:color       :muted
                                                     :content     "Registrated events"
                                                     :font-size   :m
                                                     :font-weight :extra-bold
                                                     :line-height :block}]]]
                        (-> event-handlers keys vector/abc-items)))))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [current-event @(r/subscribe [:x.db/get-item [:developer-tools :event-browser/meta-items :current-event]])]
          [event-view]
          [event-list]))
