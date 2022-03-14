
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.views
    (:require [mid-fruits.candy                         :refer [param return]]
              [mid-fruits.map                           :as map]
              [mid-fruits.pretty                        :as pretty]
              [mid-fruits.string                        :as string]
              [mid-fruits.vector                        :as vector]
              [x.app-core.api                           :as a]
              [x.app-elements.api                       :as elements]
              [x.app-developer.re-frame-browser.helpers :as re-frame-browser.helpers]))



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
                      [:i.x-icon {:style {:opacity ".5" :width "100%"} :data-icon-family "material-icons-filled"}
                                 (param icon)]
                      [icon-button-label label true]]
                [:div.x-clickable {:on-click #(a/dispatch on-click)
                                   :style {:padding "0 12px" :min-width "60px"}}
                                  [:i.x-icon {:style {:width "100%"} :data-icon-family "material-icons-filled"}
                                             (param icon)]
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
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])
        root-level?  @(a/subscribe [:re-frame-browser/root-level?])]
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
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       [:div {:style {:font-weight "500" :font-size "12px" :opacity ".5"}}
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
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       [icon-button {:icon "remove" :label "Dec" :on-click [:db/apply-item! current-path dec]}]))

(defn increase-integer-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       [icon-button {:icon "add" :label "Inc" :on-click [:db/apply-item! current-path inc]}]))

(defn swap-boolean-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       (if-let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
               [icon-button {:icon "task_alt"       :label "True"  :on-click [:db/toggle-item! current-path]}]
               [icon-button {:icon "do_not_disturb" :label "False" :on-click [:db/toggle-item! current-path]}])))

(defn toggle-data-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [show-data? @(a/subscribe [:re-frame-browser/get-meta-item :show-data?])]
          [icon-button {:icon "code_off" :label "Hide data" :on-click [:re-frame-browser/toggle-data-view!]}]
          [icon-button {:icon "code"     :label "Show data" :on-click [:re-frame-browser/toggle-data-view!]}]))

(defn edit-string-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [toggle-event [:db/toggle-item! [:developer :re-frame-browser/meta-items :edit-string?]]]
       (if-let [edit-string? @(a/subscribe [:re-frame-browser/get-meta-item :edit-string?])]
               [icon-button {:icon "edit_off" :label "Done" :on-click toggle-event}]
               [icon-button {:icon "edit"     :label "Edit" :on-click toggle-event}])))

(defn go-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(a/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "home" :label "Root" :disabled? true}]
          [icon-button {:icon "home" :label "Root" :on-click [:re-frame-browser/go-to! []]}]))

(defn go-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(a/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "chevron_left" :label "Go up" :disabled? true}]
          [icon-button {:icon "chevron_left" :label "Go up" :on-click [:re-frame-browser/go-up!]}]))

(defn remove-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [root-level? @(a/subscribe [:re-frame-browser/root-level?])]
          [icon-button {:icon "delete" :label "Remove" :disabled? true}]
          (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])
                remove-event [:db/move-item! current-path [:developer :re-frame-browser/meta-items :bin]]]
               [icon-button {:icon "delete" :label "Remove" :on-click remove-event}])))

(defn recycle-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       (if-let [bin @(a/subscribe [:re-frame-browser/get-meta-item :bin])]
               (let [revert-event [:db/move-item! [:developer :re-frame-browser/meta-items :bin] current-path]]
                    [icon-button {:icon "recycling" :label "Restore" :on-click revert-event}]))))

(defn dispatch-event-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
       [icon-button {:icon "local_fire_department" :label "Dispatch" :on-click current-item}]))

(defn toggle-subscription-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [subscribe? @(a/subscribe [:re-frame-browser/get-meta-item :subscribe?])]
          [icon-button {:icon "pause_circle" :label "Unsubscribe" :on-click [:re-frame-browser/toggle-subscription!]}]
          [icon-button {:icon "play_circle"  :label "Subscribe"   :on-click [:re-frame-browser/toggle-subscription!]}]))

(defn toggle-visibility-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [root-level?  @(a/subscribe [:re-frame-browser/root-level?])
        show-hidden? @(a/subscribe [:re-frame-browser/get-meta-item :show-hidden?])]
       (cond (and root-level? show-hidden?)
             [icon-button {:icon "visibility_off" :label "Hide hidden" :on-click [:re-frame-browser/toggle-visibility!]}]
             (and root-level? (not show-hidden?))
             [icon-button {:icon "visibility"     :label "Show hidden" :on-click [:re-frame-browser/toggle-visibility!]}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toolbar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [& tools]
  (letfn [(f [%1 %2] (conj %1 [%2]))]
         (reduce f [:div {:style {:display "flex" :margin-bottom "12px"}}] tools)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [map-key]
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       [:div.x-clickable {:on-click #(a/dispatch [:re-frame-browser/go-to! (vector/conj-item current-path map-key)])}
                         (if (string? map-key)
                             (string/quotes map-key)
                             (str           map-key))]))

(defn map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])
        root-level?  @(a/subscribe [:re-frame-browser/root-level?])
        show-hidden? @(a/subscribe [:re-frame-browser/get-meta-item :show-hidden?])
        map-keys      (-> current-item map/get-keys vector/abc-items)]
       [:div [header "map"]
             [toolbar go-home-button go-up-button remove-item-button toggle-data-view-button toggle-visibility-button]
             [horizontal-line]
             (if (empty? current-item) "Empty")
             (letfn [(f [%1 %2] (if (or show-hidden? (-> %2 re-frame-browser.helpers/map-item-hidden? not)
                                                     (not root-level?))
                                    (conj   %1 [map-key %2])
                                    (return %1)))]
                    (reduce f [:div] map-keys))
             (if-let [show-data? @(a/subscribe [:re-frame-browser/get-meta-item :show-data?])]
                     [:pre {:style {:margin-top "24px" :font-size "12px"}}
                           (pretty/mixed->string current-item)])]))

(defn vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
       [:div [header "vector"]
             [toolbar go-home-button go-up-button remove-item-button toggle-data-view-button]
             [horizontal-line]
             (if (empty? current-item) "Empty")
             (letfn [(f [%1 %2] (conj %1 [:div (cond (nil?    %2) (str "nil")
                                                     (string? %2) (string/quotes %2)
                                                     :else        (str           %2))]))]
                    (reduce f [:div] current-item))
             (if-let [show-data? @(a/subscribe [:re-frame-browser/get-meta-item :show-data?])]
                     [:pre {:style {:margin-top "24px" :font-size "12px"}}
                           (pretty/mixed->string current-item)])]))

(defn boolean-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "boolean"]
        [toolbar go-home-button go-up-button remove-item-button swap-boolean-button]
        [horizontal-line]
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn integer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "integer"]
        [toolbar go-home-button go-up-button remove-item-button decrease-integer-button increase-integer-button]
        [horizontal-line]
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn string-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "string"]
        [toolbar go-home-button go-up-button remove-item-button edit-string-button]
        [horizontal-line]
        (if-let [edit-string? @(a/subscribe [:re-frame-browser/get-meta-item :edit-string?])]
                (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
                     [elements/text-field {:value-path current-path}])
                (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
                     [:div (string/quotes current-item)]))])

(defn keyword-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "keyword"]
        [toolbar go-home-button go-up-button remove-item-button]
        [horizontal-line]
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn component-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "component"]
        [toolbar go-home-button go-up-button remove-item-button]
        [horizontal-line]
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
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
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])

(defn subscribed-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item     @(a/subscribe [:re-frame-browser/get-current-item])
        subscribed-value @(a/subscribe current-item)]
       [:div {:style {:color "var( --color-highlight )" :margin-top "24px"}}
             [:pre {:style {:white-space :break-spaces :width "100%"}}
                   (pretty/mixed->string subscribed-value)]]))

(defn subscription-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "subscription-vector"]
        [toolbar go-home-button go-up-button remove-item-button toggle-subscription-button]
        [horizontal-line]
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])
        (if-let [subscribe? @(a/subscribe [:re-frame-browser/get-meta-item :subscribe?])]
                [subscribed-value])])

(defn unknown-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "unknown"]
        [toolbar go-home-button go-up-button remove-item-button]
        [horizontal-line]
        (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
             [:div (str current-item)])])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn database-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-item @(a/subscribe [:re-frame-browser/get-current-item])]
       (cond (map? current-item)                                 [map-item]
             (a/event-vector? current-item {:strict-mode? true}) [event-vector-item]
             (a/subscription-vector? current-item)               [subscription-vector-item]
             (vector?                current-item)               [vector-item]
             (boolean?               current-item)               [boolean-item]
             (integer?               current-item)               [integer-item]
             (string?                current-item)               [string-item]
             (keyword?               current-item)               [keyword-item]
             (var?                   current-item)               [component-item]
             (nil?                   current-item)               [nil-item]
             :else                                               [unknown-item])))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div {:style {:color "var( --color-muted )" :overflow-x "auto" :padding "12px 6px" :width "100%"}}
        [database-item]])
