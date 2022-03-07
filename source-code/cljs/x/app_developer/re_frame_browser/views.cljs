
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.re-frame-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.mixed     :as mixed]
              [mid-fruits.pretty    :as pretty]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]))



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
  [:div.x-re-frame-browser--header--item-type {:style {:opacity ".5" :padding-right "12px"}}
                                              (str "(" item-type ")")])

(defn item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])
        root-level?  @(a/subscribe [:re-frame-browser/root-level?])]
       [:div.x-re-frame-browser--header--item-label {:style {:font-weight "500" :font-size "16px"}}
                                                    (if root-level? (str "Re-Frame DB")
                                                                    (last current-path))]))

(defn label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div.x-re-frame-browser--label-bar {:style {:display "flex"}}
                                      [item-type-label item-type]
                                      [item-label]])

(defn breadcrumbs
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [current-path @(a/subscribe [:re-frame-browser/get-current-path])]
       [:div.x-re-frame-browser--header--current-path {:style {:font-weight "500" :font-size "12px" :opacity ".5"}}
                                                      (string/join current-path " / ")]))

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-type]
  [:div.x-re-frame-browser--header {:style {:margin-bottom "12px"}}
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
         (reduce f [:div.x-re-frame-browser--toolbar {:style {:display "flex" :margin-bottom "12px"}}] tools)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [current-path] :as body-props} map-key]
  [:div.x-clickable {:on-click #(a/dispatch [:re-frame-browser/navigate! (vector/conj-item current-path map-key)])
                     :style (if (map-item-hidden? map-key body-props) {:opacity ".65"})}
                    (if (string? map-key)
                        (string/quotes map-key)
                        (str           map-key))])

(defn map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [current-item show-original?] :as body-props}]
  (let [map-keys (vector/abc-items (map/get-keys current-item))]
       [:div.x-re-frame-browser--map-item
         [header          body-id body-props "map"]
         [toolbar         body-id body-props go-home-button go-up-button remove-item-button
                                             toggle-original-view-button toggle-visibility-button]
         [horizontal-line body-id body-props]
         (if (empty? current-item) "Empty")
         (reduce #(if (render-map-item? %2 body-props)
                      (conj             %1 [map-key body-id body-props %2])
                      (return           %1))
                  [:div.x-re-frame-browser--map-item--keys]
                  (param map-keys))
         (if show-original? [:pre.x-re-frame-browser--map-item--original-view
                               {:style {:margin-top "24px" :font-size "12px"}}
                               (pretty/mixed->string current-item)])]))

(defn vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [current-item show-original?] :as body-props}]
  [:div.x-re-frame-browser--vector-item
    [header          body-id body-props "vector"]
    [toolbar         body-id body-props go-home-button go-up-button remove-item-button
                                        toggle-original-view-button]
    [horizontal-line body-id body-props]
    (if (empty? current-item) "Empty")
    (reduce #(conj %1 [:div (cond (nil?    %2) (str "nil")
                                  (string? %2) (string/quotes %2)
                                  :else        (str           %2))])
             [:div.x-re-frame-browser--vector-item--items]
             (param current-item))
    (if show-original? [:pre.x-re-frame-browser--map-item--original-view
                          {:style {:margin-top "24px" :font-size "12px"}}
                          (pretty/mixed->string current-item)])])

(defn boolean-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [current-item current-path] :as body-props}]
  [:div.x-re-frame-browser--boolean-item
    [header          body-id body-props "boolean"]
    [toolbar         body-id body-props go-home-button go-up-button remove-item-button swap-boolean-button]
    [horizontal-line body-id body-props]
    [:div.x-re-frame-browser--item (str current-item)]])

(defn integer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [current-item] :as body-props}]
  [:div.x-re-frame-browser--integer-item
    [header          body-id body-props "integer"]
    [toolbar         body-id body-props go-home-button go-up-button remove-item-button
                                        decrease-integer-button increase-integer-button]
    [horizontal-line body-id body-props]
    [:div.x-re-frame-browser--item (str current-item)]])

(defn string-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [current-item current-path edit-string?] :as body-props}]
  [:div.x-re-frame-browser--string-item
    [header          body-id body-props "string"]
    [toolbar         body-id body-props go-home-button go-up-button remove-item-button
                                        edit-string-button]
    [horizontal-line body-id body-props]
    (if edit-string? [elements/text-field {:value-path current-path}]
                     [:div.x-re-frame-browser--item (string/quotes current-item)])])

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
        [:div (str current-item)]
        (if-let [subscribe? @(a/subscribe [:re-frame-browser/get-meta-item :subscribe?])]
                [subscribed-value])])

(defn unknown-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:div [header "unknown"]
        [toolbar go-home-button go-up-button remove-item-button]
        [horizontal-line]
        [:div (str current-item)]])



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
