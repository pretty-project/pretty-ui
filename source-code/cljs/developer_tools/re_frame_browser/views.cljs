
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.re-frame-browser.views
    (:require [candy.api                               :refer [return]]
              [developer-tools.re-frame-browser.config :as re-frame-browser.config]
              [elements.api                            :as elements]
              [map.api                                 :as map]
              [pretty.print                            :as pretty]
              [re-frame.api                            :as r]
              [string.api                              :as string]
              [syntax.api                              :as syntax]
              [vector.api                              :as vector]
              [x.environment.api                       :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn horizontal-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div {:style {:width "100%" :height "1px" :margin-bottom "24px"
                 :border "1px dashed var( --border-color-highlight)"}}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-type-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div {:style {:opacity ".5" :padding-right "12px" :font-size "var(--font-size-xs)" :font-weight "500"
                 :line-height "18px"}}
        item-type])

(defn item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])
        root-level?  @(r/subscribe [:developer-tools.re-frame-browser/root-level?])]
       [:div {:style {:font-weight "500" :font-size "16px"}}
             (if root-level? (str "Re-Frame DB")
                             (last current-path))]))

(defn label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div {}
        [item-label]
        [item-type-label item-type]])

(defn breadcrumbs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])]
       [:div {:style {:font-weight "500" :font-size "12px" :opacity ".5" :min-height "24px"}}
             (string/join current-path " • ")]))

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div {:style {}}
        [label-bar item-type]
        [breadcrumbs]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-integer-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])]
       [elements/icon-button ::decrease-integer-icon-button
                             {:icon :remove :label "Dec" :on-click [:x.db/apply-item! current-path dec]
                              :hover-color :highlight :height :3xl :width :3xl}]))

(defn increase-integer-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])]
       [elements/icon-button ::increase-integer-icon-button
                             {:icon :add :label "Inc" :on-click [:x.db/apply-item! current-path inc]
                              :hover-color :highlight :height :3xl :width :3xl}]))

(defn swap-boolean-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])]
       (if-let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
               [elements/icon-button ::swap-boolean-icon-button
                                     {:icon :task_alt       :label "True"  :on-click [:x.db/toggle-item! current-path]
                                      :hover-color :highlight :height :3xl :width :3xl}]
               [elements/icon-button ::swap-boolean-icon-button
                                     {:icon :do_not_disturb :label "False" :on-click [:x.db/toggle-item! current-path]
                                      :hover-color :highlight :height :3xl :width :3xl}])))

(defn toggle-data-view-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [show-data? @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :show-data?])]
          [elements/icon-button ::hide-data-icon-icon-button
                                {:icon :code_off :label "Hide data" :on-click [:developer-tools.re-frame-browser/toggle-data-view!]
                                 :hover-color :highlight :height :3xl :width :3xl}]
          [elements/icon-button ::show-data-icon-icon-button
                                {:icon :code     :label "Show data" :on-click [:developer-tools.re-frame-browser/toggle-data-view!]
                                 :hover-color :highlight :height :3xl :width :3xl}]))

(defn go-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:developer-tools.re-frame-browser/root-level?])]
          [elements/icon-button ::go-home-icon-button
                                {:icon :home :label "Root" :disabled? true
                                 :hover-color :highlight :height :3xl :width :3xl}]
          [elements/icon-button ::go-home-icon-button
                                {:icon :home :label "Root" :on-click [:developer-tools.re-frame-browser/go-to! []]
                                 :hover-color :highlight :height :3xl :width :3xl}]))

(defn go-up-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:developer-tools.re-frame-browser/root-level?])]
          [elements/icon-button ::go-up-icon-button
                                {:icon :chevron_left :label "Go up" :disabled? true
                                 :hover-color :highlight :height :3xl :width :3xl}]
          [elements/icon-button ::go-up-icon-button
                                {:icon :chevron_left :label "Go up" :on-click [:developer-tools.re-frame-browser/go-up!]
                                 :hover-color :highlight :height :3xl :width :3xl}]))

(defn remove-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:developer-tools.re-frame-browser/root-level?])]
          [elements/icon-button ::remove-item-icon-button
                                {:icon :delete :label "Remove" :disabled? true
                                 :hover-color :highlight :height :3xl :width :3xl}]
          (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])
                remove-event [:x.db/move-item! current-path [:developer-tools :re-frame-browser/meta-items :bin]]]
               [elements/icon-button ::remove-item-icon-button
                                     {:icon :delete :label "Remove" :on-click remove-event
                                      :hover-color :highlight :height :3xl :width :3xl}])))

(defn edit-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:developer-tools.re-frame-browser/root-level?])]
          [elements/icon-button ::edit-item-icon-button
                                {:icon :edit :label "Edit" :disabled? true
                                 :hover-color :highlight :height :3xl :width :3xl}]
          (if-let [edit-item? @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :edit-item?])]
                  [elements/icon-button ::edit-item-icon-button
                                        {:icon :edit_off :label "Save" :on-click [:developer-tools.re-frame-browser/toggle-edit-item-mode!]
                                         :hover-color :highlight :height :3xl :width :3xl}]
                  [elements/icon-button ::edit-item-icon-button
                                        {:icon :edit     :label "Edit" :on-click [:developer-tools.re-frame-browser/toggle-edit-item-mode!]
                                         :hover-color :highlight :height :3xl :width :3xl}])))

(defn recycle-item-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])]
       (if-let [bin @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :bin])]
               (let [revert-event [:x.db/move-item! [:developer-tools :re-frame-browser/meta-items :bin] current-path]]
                    [elements/icon-button ::recycle-item-icon-button
                                          {:icon :recycling :label "Restore" :on-click revert-event
                                           :hover-color :highlight :height :3xl :width :3xl}]))))

(defn dispatch-event-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
       [elements/icon-button ::dispatch-event-icon-button
                             {:icon :local_fire_department :label "Dispatch" :on-click current-item
                              :hover-color :highlight :height :3xl :width :3xl}]))

(defn toggle-subscription-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [subscribe? @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :subscribe?])]
          [elements/icon-button ::toggle-subscription-icon-button
                                {:icon :pause_circle :label "Unsubscribe" :on-click [:developer-tools.re-frame-browser/toggle-subscription!]
                                 :hover-color :highlight :height :3xl :width :3xl}]
          [elements/icon-button ::toggle-subscription-icon-button
                                {:icon :play_circle  :label "Subscribe"   :on-click [:developer-tools.re-frame-browser/toggle-subscription!]
                                 :hover-color :highlight :height :3xl :width :3xl}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toolbar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [& tools]
  (letfn [(f [%1 %2] (conj %1 [%2]))]
         (reduce f [:div {:style {:display "flex" :margin-bottom "12px"}}] tools)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-data
  []
  (if-let [show-data? @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :show-data?])]
          (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
               [:<> [elements/horizontal-separator {:size :xxl}]
                    [:pre {:style {:font-size "12px"}}
                          (pretty/mixed->string current-item)]])))
(defn edit-item
  []
  (if-let [edit-item? @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :edit-item?])]
          [:<> [elements/multiline-field ::edit-item-field
                                         {:indent     {:top :xxl}
                                          :value-path [:developer-tools :re-frame-browser/meta-items :edited-item]}]]))

(defn map-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [map-key system-key?]
  (let [current-path @(r/subscribe [:developer-tools.re-frame-browser/get-current-path])]
       [:button {:data-clickable true :style {:display :block}
                 :on-click #(r/dispatch [:developer-tools.re-frame-browser/go-to! (vector/conj-item current-path map-key)])
                 :on-mouse-up #(x.environment/blur-element!)}
                (cond (string? map-key) (syntax/quotes map-key)
                      (nil?    map-key) (str           "nil")
                      :return           (str           map-key))]))

(defn map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])
        root-level?  @(r/subscribe [:developer-tools.re-frame-browser/root-level?])
        map-keys      (-> current-item map/get-keys vector/abc-items)
        system-keys   (vector/keep-items   map-keys re-frame-browser.config/SYSTEM-KEYS)
        app-keys      (vector/remove-items map-keys re-frame-browser.config/SYSTEM-KEYS)]
       [:div [header (str "map, "(count map-keys)" item(s)")]
             [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button toggle-data-view-icon-button edit-item-icon-button]
             [horizontal-line]
             (if (empty? current-item) "Empty")
             (letfn [(f [%1 %2] (conj %1 [map-key %2]))]
                    (if root-level? [:<> (reduce f [:div {:style {}}]                app-keys)
                                         (reduce f [:div {:style {:opacity 0.5}}] system-keys)]
                                    [:<> (reduce f [:div {:style {}}]                map-keys)]))
             [show-data]
             [edit-item]]))

(defn vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
       [:div [header (str "vector, " (count current-item) " item(s)")]
             [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button toggle-data-view-icon-button edit-item-icon-button]
             [horizontal-line]
             (if (empty? current-item) "Empty")
             (letfn [(f [%1 %2] (conj %1 [:div (cond (nil?    %2) (str "nil")
                                                     (string? %2) (syntax/quotes %2)
                                                     :return      (str           %2))]))]
                    (reduce f [:div] current-item))
             [show-data]
             [edit-item]]))

(defn boolean-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "boolean"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button swap-boolean-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn integer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "integer"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button decrease-integer-icon-button increase-integer-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn string-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
       [:div [header (str "string, "(count current-item) " char.")]
             [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button edit-item-icon-button]
             [horizontal-line]
             [:div (syntax/quotes current-item)]
             [edit-item]]))

(defn keyword-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "keyword"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button edit-item-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])
        [edit-item]])

(defn component-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "component"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn nil-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "nil"]
        [toolbar go-home-icon-button go-up-icon-button recycle-item-icon-button]
        [horizontal-line]
        [:div (str "nil")]])

(defn event-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "event-vector"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button dispatch-event-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn subscribed-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item     @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])
        subscribed-value @(r/subscribe current-item)]
       [:div {:style {:color "var( --color-highlight )" :margin-top "24px"}}
             [:pre {:style {:white-space :break-spaces :width "100%"}}
                   (pretty/mixed->string subscribed-value)]]))

(defn subscription-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "subscription-vector"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button toggle-subscription-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])
        (if-let [subscribe? @(r/subscribe [:developer-tools.re-frame-browser/get-meta-item :subscribe?])]
                [subscribed-value])])

(defn unknown-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "unknown"]
        [toolbar go-home-icon-button go-up-icon-button remove-item-icon-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
             [:div (str current-item)])])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn database-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:developer-tools.re-frame-browser/get-current-item])]
       (cond (map? current-item)                   [map-item]
             ;(r/event-vector?        current-item) [event-vector-item]
             ;(r/subscription-vector? current-item) [subscription-vector-item]
             (vector?                current-item) [vector-item]
             (boolean?               current-item) [boolean-item]
             (integer?               current-item) [integer-item]
             (string?                current-item) [string-item]
             (keyword?               current-item) [keyword-item]
             (var?                   current-item) [component-item]
             (nil?                   current-item) [nil-item]
             :return                               [unknown-item])))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div {:style {:color "var( --color-muted )" :overflow-x "auto" :padding "0 12px 12px 12px" :width "100%"}}
        [database-item]])
