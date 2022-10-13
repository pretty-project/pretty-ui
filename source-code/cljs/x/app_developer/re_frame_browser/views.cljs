
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.views
    (:require [mid-fruits.candy                        :refer [param return]]
              [mid-fruits.map                          :as map]
              [mid-fruits.pretty                       :as pretty]
              [mid-fruits.string                       :as string]
              [mid-fruits.vector                       :as vector]
              [re-frame.api                            :as r]
              [x.app-developer.re-frame-browser.config :as re-frame-browser.config]
              [x.app-elements.api                      :as elements]
              [x.app-environment.api                   :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn horizontal-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div {:style {:width "100%" :height "1px" :margin-bottom "24px"
                 :border "1px dashed var( --border-color-highlight)"}}])

(defn icon-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [label & [disabled?]]
  [:span {:style {:display "block" :text-align "center" :font-size "10px" :font-weight "600"
                  :opacity (if disabled? ".5" "1")}}
         (param label)])

(defn icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [disabled? icon label on-click]}]
  (if disabled? [:div {:style {:padding "0 12px" :min-width "60px"}}
                      [:div.x-icon.x-element {:style {:opacity ".5" :width "100%"}}
                                             [:i.x-icon--body {:data-icon-family :material-icons-filled} icon]]
                      [icon-button-label label true]]
                [:button {:data-clickable true
                          :on-click    #(r/dispatch on-click)
                          :on-mouse-up #(environment/blur-element!)
                          :style {:display :block :padding "0 12px" :min-width "60px"}}
                         [:div.x-icon.x-element {:style {:width "100%"}}
                                                [:i.x-icon--body {:data-icon-family :material-icons-filled} icon]]
                         [icon-button-label label]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-type-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div {:style {:opacity ".5" :padding-right "12px"}}
        (str "(" item-type ")")])

(defn item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])
        root-level?  @(r/subscribe [:re-frame-browser/root-level?])]
       [:div {:style {:font-weight "500" :font-size "16px"}}
             (if root-level? (str "Re-Frame DB")
                             (last current-path))]))

(defn label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div {:style {:display "flex"}}
        [item-type-label item-type]
        [item-label]])

(defn breadcrumbs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])]
       [:div {:style {:font-weight "500" :font-size "12px" :opacity ".5" :min-height "24px"}}
             (string/join current-path " / ")]))

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div {:style {:margin-bottom "12px"}}
        [label-bar item-type]
        [breadcrumbs]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn decrease-integer-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])]
       [icon-button {:icon "remove" :label "Dec" :on-click [:db/apply-item! current-path dec]}]))

(defn increase-integer-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])]
       [icon-button {:icon "add" :label "Inc" :on-click [:db/apply-item! current-path inc]}]))

(defn swap-boolean-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])]
       (if-let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
               [icon-button {:icon "task_alt"       :label "True"  :on-click [:db/toggle-item! current-path]}]
               [icon-button {:icon "do_not_disturb" :label "False" :on-click [:db/toggle-item! current-path]}])))

(defn toggle-data-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [show-data? @(r/subscribe [:re-frame-browser/get-meta-item :show-data?])]
          [icon-button {:icon "code_off" :label "Hide data" :on-click [:re-frame-browser/toggle-data-view!]}]
          [icon-button {:icon "code"     :label "Show data" :on-click [:re-frame-browser/toggle-data-view!]}]))

(defn go-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "home" :label "Root" :disabled? true}]
          [icon-button {:icon "home" :label "Root" :on-click [:re-frame-browser/go-to! []]}]))

(defn go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "chevron_left" :label "Go up" :disabled? true}]
          [icon-button {:icon "chevron_left" :label "Go up" :on-click [:re-frame-browser/go-up!]}]))

(defn remove-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "delete" :label "Remove" :disabled? true}]
          (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])
                remove-event [:db/move-item! current-path [:developer :re-frame-browser/meta-items :bin]]]
               [icon-button {:icon "delete" :label "Remove" :on-click remove-event}])))

(defn edit-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(r/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "edit" :label "Edit" :disabled? true}]
          (if-let [edit-item? @(r/subscribe [:re-frame-browser/get-meta-item :edit-item?])]
                  [icon-button {:icon "edit_off" :label "Save" :on-click [:re-frame-browser/toggle-edit-item-mode!]}]
                  [icon-button {:icon "edit"     :label "Edit" :on-click [:re-frame-browser/toggle-edit-item-mode!]}])))

(defn recycle-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])]
       (if-let [bin @(r/subscribe [:re-frame-browser/get-meta-item :bin])]
               (let [revert-event [:db/move-item! [:developer :re-frame-browser/meta-items :bin] current-path]]
                    [icon-button {:icon "recycling" :label "Restore" :on-click revert-event}]))))

(defn dispatch-event-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
       [icon-button {:icon "local_fire_department" :label "Dispatch" :on-click current-item}]))

(defn toggle-subscription-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [subscribe? @(r/subscribe [:re-frame-browser/get-meta-item :subscribe?])]
          [icon-button {:icon "pause_circle" :label "Unsubscribe" :on-click [:re-frame-browser/toggle-subscription!]}]
          [icon-button {:icon "play_circle"  :label "Subscribe"   :on-click [:re-frame-browser/toggle-subscription!]}]))



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
  (if-let [show-data? @(r/subscribe [:re-frame-browser/get-meta-item :show-data?])]
          (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
               [:<> [elements/horizontal-separator {:size :xxl}]
                    [:pre {:style {:font-size "12px"}}
                          (pretty/mixed->string current-item)]])))
(defn edit-item
  []
  (if-let [edit-item? @(r/subscribe [:re-frame-browser/get-meta-item :edit-item?])]
          [:<> [elements/multiline-field ::edit-item-field
                                         {:indent     {:top :xxl}
                                          :value-path [:developer :re-frame-browser/meta-items :edited-item]}]]))

(defn map-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [map-key system-key?]
  (let [current-path @(r/subscribe [:re-frame-browser/get-current-path])]
       [:button {:data-clickable true :style {:display :block}
                 :on-click #(r/dispatch [:re-frame-browser/go-to! (vector/conj-item current-path map-key)])
                 :on-mouse-up #(environment/blur-element!)}
                (cond (string? map-key) (string/quotes map-key)
                      (nil?    map-key) (str           "nil")
                      :return           (str           map-key))]))

(defn map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])
        root-level?  @(r/subscribe [:re-frame-browser/root-level?])
        map-keys      (-> current-item map/get-keys vector/abc-items)
        system-keys   (vector/keep-items   map-keys re-frame-browser.config/SYSTEM-KEYS)
        app-keys      (vector/remove-items map-keys re-frame-browser.config/SYSTEM-KEYS)]
       [:div [header (str "map, "(count map-keys)" items")]
             [toolbar go-home-button go-up-button remove-item-button toggle-data-view-button edit-item-button]
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
  (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
       [:div [header (str "vector, " (count current-item) " items")]
             [toolbar go-home-button go-up-button remove-item-button toggle-data-view-button edit-item-button]
             [horizontal-line]
             (if (empty? current-item) "Empty")
             (letfn [(f [%1 %2] (conj %1 [:div (cond (nil?    %2) (str "nil")
                                                     (string? %2) (string/quotes %2)
                                                     :return      (str           %2))]))]
                    (reduce f [:div] current-item))
             [show-data]
             [edit-item]]))

(defn boolean-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "boolean"]
        [toolbar go-home-button go-up-button remove-item-button swap-boolean-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn integer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "integer"]
        [toolbar go-home-button go-up-button remove-item-button decrease-integer-button increase-integer-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn string-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
       [:div [header (str "string, "(count current-item) " char.")]
             [toolbar go-home-button go-up-button remove-item-button edit-item-button]
             [horizontal-line]
             [:div (string/quotes current-item)]
             [edit-item]]))

(defn keyword-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "keyword"]
        [toolbar go-home-button go-up-button remove-item-button edit-item-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])
        [edit-item]])

(defn component-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "component"]
        [toolbar go-home-button go-up-button remove-item-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn nil-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "nil"]
        [toolbar go-home-button go-up-button recycle-item-button]
        [horizontal-line]
        [:div (str "nil")]])

(defn event-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "event-vector"]
        [toolbar go-home-button go-up-button remove-item-button dispatch-event-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn subscribed-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item     @(r/subscribe [:re-frame-browser/get-current-item])
        subscribed-value @(r/subscribe current-item)]
       [:div {:style {:color "var( --color-highlight )" :margin-top "24px"}}
             [:pre {:style {:white-space :break-spaces :width "100%"}}
                   (pretty/mixed->string subscribed-value)]]))

(defn subscription-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "subscription-vector"]
        [toolbar go-home-button go-up-button remove-item-button toggle-subscription-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])
        (if-let [subscribe? @(r/subscribe [:re-frame-browser/get-meta-item :subscribe?])]
                [subscribed-value])])

(defn unknown-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "unknown"]
        [toolbar go-home-button go-up-button remove-item-button]
        [horizontal-line]
        (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn database-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(r/subscribe [:re-frame-browser/get-current-item])]
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
  [:div {:style {:color "var( --color-muted )" :overflow-x "auto" :padding "12px" :width "100%"}}
        [database-item]])
