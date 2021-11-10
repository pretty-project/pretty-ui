
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v1.8.6
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.database-browser
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.keyword   :as keyword]
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

(defn- map-item-hidden?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n]
  (and (keyword? n)
       (or (keyword/starts-with? n :x.)
           (keyword/starts-with? n :plugins.))))

(defn- render-map-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [n {:keys [show-hidden?]}]
  (or (not (map-item-hidden? n))
      (boolean show-hidden?)))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-path->root-level?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) item-path
  ;
  ; @return (boolean)
  [item-path]
  (= item-path []))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-current-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (vector)
  [db _]
  (let [current-path (get-in db (db/path ::settings :current-path))]
       (mixed/mixed->vector current-path)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:bin (*)
  ;   :current-path (item-path vector)
  ;   :current-item (*)
  ;   :edit-string? (boolean)
  ;   :root-level? (boolean)
  ;   :show-original? (boolean)
  ;   :subscribe? (boolean)}
  [db _]
  (let [current-path (r get-current-path db)]
       {:bin            (get-in db (db/path ::settings :bin))
        :current-path   (param current-path)
        :current-item   (get-in db current-path)
        :edit-string?   (get-in db (db/path ::settings :edit-string?))
        :root-level?    (item-path->root-level? current-path)
        :show-hidden?   (get-in db (db/path ::settings :show-hidden?))
        :show-original? (get-in db (db/path ::settings :show-original?))
        :subscribe?     (get-in db (db/path ::settings :subscribe?))}))

(a/reg-sub ::get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- navigate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) item-path
  ;
  ; @return (map)
  [db [_ item-path]]
  (-> db (assoc-in  (db/path ::settings :current-path) item-path)
         (dissoc-in (db/path ::settings :bin))
         (dissoc-in (db/path ::settings :edit-string?))
         (dissoc-in (db/path ::settings :show-original?))
         (dissoc-in (db/path ::settings :subscribe?))))

(a/reg-event-db ::navigate! navigate!)

(defn- toggle-subscription!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [subscribe? (get-in db (db/path ::settings :subscribe?))]
          (assoc-in db (db/path ::settings :subscribe?)
                       (param false))
          (assoc-in db (db/path ::settings :subscribe?)
                       (param true))))

(a/reg-event-db ::toggle-subscription! toggle-subscription!)

(defn- toggle-visibility!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [show-hidden? (get-in db (db/path ::settings :show-hidden?))]
          (assoc-in db (db/path ::settings :show-hidden?)
                       (param false))
          (assoc-in db (db/path ::settings :show-hidden?)
                       (param true))))

(a/reg-event-db ::toggle-visibility! toggle-visibility!)

(defn- toggle-original-view!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [show-original? (get-in db (db/path ::settings :show-original?))]
          (assoc-in db (db/path ::settings :show-original?)
                       (param false))
          (assoc-in db (db/path ::settings :show-original?)
                       (param true))))

(a/reg-event-db ::toggle-original-view! toggle-original-view!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::navigate-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) item-path
  (fn [_ [_ item-path]]
      [::navigate! (vector/remove-last-item item-path)]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) label
  ; @param (boolean)(opt) disabled?
  ;
  ; @return (hiccup)
  [label & [disabled?]]
  [:span {:style {:display "block" :text-align "center" :font-size "10px" :font-weight "600"
                  :opacity (if disabled? ".5" "1")}}
         (param label)])

(defn- icon-button
  [{:keys [disabled? icon label on-click]}]
  (if (boolean disabled?)
      [:div {:style {:padding "0 12px" :min-width "48px"}}
        [:i.x-icon {:style {:opacity ".5" :width "100%"} :data-icon-family "material-icons-filled"}
                   (param icon)]
        [icon-button-label label true]]
      [:div.x-toggle {:on-click #(a/dispatch on-click)
                      :style {:padding "0 12px" :min-width "48px"}}
        [:i.x-icon {:style {:width "100%"} :data-icon-family "material-icons-filled"}
                   (param icon)]
        [icon-button-label label]]))

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ; @param (string) type
  ;
  ; @return (hiccup)
  [{:keys [current-path]} type]
  [:div.x-database-browser--header
    {:style {:margin-bottom "12px"}}
    [:div.x-database-browser--header--label-bar
      {:style {:display "flex"}}
      [:div.x-database-browser--header--item-type
        {:style {:opacity ".5" :padding-right "12px"}}
        (str "(" type ")")]
      [:div.x-database-browser--header--item-label
        {:style {:font-weight "500" :font-size "16px"}}
        (if (item-path->root-level? current-path)
            (str "Re-Frame DB")
            (last current-path))]]
    [:div.x-database-browser--header--current-path
      {:style {:font-weight "500" :font-size "12px" :opacity ".5"}}
      (string/join current-path " / ")]])

(defn- decrease-integer-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-path]}]
  [icon-button {:icon "remove" :label "Dec" :on-click [:x.app-db/apply! current-path dec]}])

(defn- increase-integer-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-path]}]
  [icon-button {:icon "add" :label "Inc" :on-click [:x.app-db/apply! current-path inc]}])

(defn- swap-boolean-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (boolean)
  ;   :current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item current-path]}]
  (if (boolean current-item)
      [icon-button {:icon "task_alt"       :label "True"  :on-click [:x.app-db/apply! current-path not]}]
      [icon-button {:icon "do_not_disturb" :label "False" :on-click [:x.app-db/apply! current-path not]}]))

(defn- toggle-original-view-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:show-original? (boolean)}
  ;
  ; @return (hiccup)
  [{:keys [show-original?]}]
  (if (boolean show-original?)
      [icon-button {:icon "code_off" :label "Hide data" :on-click [::toggle-original-view!]}]
      [icon-button {:icon "code"     :label "Show data" :on-click [::toggle-original-view!]}]))

(defn- edit-string-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:edit-string? (boolean)}
  ;
  ; @return (hiccup)
  [{:keys [edit-string?]}]
  (let [toggle-event [:x.app-db/apply! (db/path ::settings :edit-string?) not]]
       (if (boolean edit-string?)
           [icon-button {:icon "edit_off" :label "Done" :on-click toggle-event}]
           [icon-button {:icon "edit"     :label "Edit" :on-click toggle-event}])))

(defn- go-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-path]}]
  (if (item-path->root-level? current-path)
      [icon-button {:icon "home" :label "Root" :disabled? true}]
      [icon-button {:icon "home" :label "Root" :on-click [::navigate! []]}]))

(defn- navigate-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-path]}]
  (if (item-path->root-level? current-path)
      [icon-button {:icon "chevron_left" :label "Go up" :disabled? true}]
      [icon-button {:icon "chevron_left" :label "Go up" :on-click [::navigate-up! current-path]}]))

(defn- remove-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-path]}]
  (let [remove-event [:x.app-db/move-item! current-path (db/path ::settings :bin)]]
       (if (item-path->root-level? current-path)
           [icon-button {:icon "delete" :label "Remove" :disabled? true}]
           [icon-button {:icon "delete" :label "Remove" :on-click remove-event}])))

(defn- recycle-item-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:bin (*)(opt)
  ;   :current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [bin current-path]}]
  (let [revert-event [:x.app-db/move-item! (db/path ::settings :bin) current-path]]
       (if (some? bin)
           [icon-button {:icon "recycling" :label "Restore" :on-click revert-event}])))

(defn- dispatch-event-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item]}]
  [icon-button {:icon "local_fire_department" :label "Dispatch" :on-click current-item}])

(defn- toggle-subscription-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (subscription vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item subscribe?]}]
  (if (boolean subscribe?)
      [icon-button {:icon "pause_circle" :label "Unsubscribe" :on-click [::toggle-subscription!]}]
      [icon-button {:icon "play_circle"  :label "Subscribe"   :on-click [::toggle-subscription!]}]))

(defn- toggle-visibility-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)
  ;   :show-hidden? (boolean)}
  ;
  ; @return (hiccup)
  [{:keys [current-item current-path show-hidden?]}]
  (cond (and (item-path->root-level? current-path)
             (boolean show-hidden?))
        [icon-button {:icon "visibility_off" :label "Hide hidden" :on-click [::toggle-visibility!]}]
        (and (item-path->root-level? current-path)
             (not show-hidden?))
        [icon-button {:icon "visibility" :label "Show hidden" :on-click [::toggle-visibility!]}]))

(defn- toolbar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ; @param (list of components) tools
  ;
  ; @return (hiccup)
  [view-props & tools]
  (reduce (fn [%1 %2] (vector/conj-item %1 [%2 view-props]))
          [:div.x-database-browser--toolbar
            {:style {:display "flex" :margin-bottom "12px"}}]
          (param tools)))

(defn- horizontal-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_]
  [:div.x-database-browser--horizontal-line
    {:style {:width "100%" :height "1px" :border "1px dashed var( --border-color-highlight)"
             :margin-bottom "24px"}}])

(defn- map-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-path (item-path vector)}
  ; @param (*) map-key
  ;
  ; @return (hiccup)
  [{:keys [current-path]} map-key]
  [:div.x-toggle
    {:on-click #(a/dispatch [::navigate! (vector/conj-item current-path map-key)])
     :style (if (map-item-hidden? map-key) {:opacity ".65"})}
    (if (string? map-key)
        (string/quotes map-key)
        (str           map-key))])

(defn- map-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (*)
  ;   :current-path (item-path vector)
  ;   :show-original? (boolean)}
  ;
  ; @return (hiccup)
  [{:keys [current-item show-original?] :as view-props}]
  (let [map-keys (vector/abc (map/get-keys current-item))]
       [:div.x-database-browser--map-item
         [header  view-props "map"]
         [toolbar view-props go-home-button navigate-up-button remove-item-button toggle-original-view-button toggle-visibility-button]
         [horizontal-line view-props]
         (if-not (map/nonempty? current-item)
                 (str "Empty"))
         (reduce (fn [%1 %2] (if (render-map-item? %2 view-props)
                                 (vector/conj-item %1 [map-key view-props %2])
                                 (return           %1)))
                 [:div.x-database-browser--map-item--keys]
                 (param map-keys))
         (if show-original? [:pre.x-database-browser--map-item--original-view
                               {:style {:margin-top "24px" :font-size "12px"}}
                               (pretty/mixed->string current-item)])]))

(defn- vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (vector)
  ;   :current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  (reduce (fn [%1 %2] (vector/conj-item %1 [:div (if (string? %2)
                                                     (string/quotes %2)
                                                     (str           %2))]))
          [:div.x-database-browser--vector-item
            [header  view-props "vector"]
            [toolbar view-props go-home-button navigate-up-button remove-item-button]
            [horizontal-line view-props]
            (if-not (vector/nonempty? current-item)
                    (str "Empty"))]
          (param current-item)))

(defn- boolean-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (boolean)
  ;   :current-path (item-path vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item current-path] :as view-props}]
  [:div.x-database-browser--boolean-item
    [header  view-props "boolean"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button swap-boolean-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]])

(defn- integer-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (integer)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  [:div.x-database-browser--integer-item
    [header  view-props "integer"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button
             decrease-integer-button increase-integer-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]])

(defn- string-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (string)
  ;   :current-path (item-path vector)
  ;   :edit-string? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [{:keys [current-item current-path edit-string?] :as view-props}]
  [:div.x-database-browser--string-item
    [header  view-props "string"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button edit-string-button]
    [horizontal-line view-props]
    (if (boolean edit-string?)
        [elements/text-field {:value-path current-path}]
        [:div.x-database-browser--item (string/quotes current-item)])])

(defn- keyword-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (keyword)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  [:div.x-database-browser--keyword-item
    [header  view-props "keyword"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]])

(defn- component-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (component)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  [:div.x-database-browser--component-item
    [header  view-props "component"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]])

(defn- nil-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [view-props]
  [:div.x-database-browser--nil-item
    [header  view-props "nil"]
    [toolbar view-props go-home-button navigate-up-button recycle-item-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str "nil")]])

(defn- event-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  [:div.x-database-browser--event-vector-item
    [header  view-props "event-vector"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button dispatch-event-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]])

(defn- subscribed-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (subscription vector)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  [:div.x-database-browser--subscribed-value
    {:style {:color "var( --color-highlight )" :margin-top "24px"}}
    [components/content {:subscriber current-item}]])

(defn- subscription-vector-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (subscription vector)
  ;   :subscribe? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [{:keys [current-item subscribe?] :as view-props}]
  [:div.x-database-browser--subscription-vector-item
    [header  view-props "subscription-vector"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button toggle-subscription-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]
    (if subscribe? [subscribed-value view-props])])

(defn- unknown-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (*)}
  ;
  ; @return (hiccup)
  [{:keys [current-item] :as view-props}]
  [:div.x-database-browser--unknown-item
    [header  view-props "unknown"]
    [toolbar view-props go-home-button navigate-up-button remove-item-button]
    [horizontal-line view-props]
    [:div.x-database-browser--item (str current-item)]])

(defn- database-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:current-item (*)}
  ;
  ; @return (component)
  [{:keys [current-item] :as view-props}]
  (cond (map? current-item)                                 [map-item                 view-props]
        (a/event-vector? current-item {:strict-mode? true}) [event-vector-item        view-props]
        (a/subscription-vector? current-item)               [subscription-vector-item view-props]
        (vector? current-item)                              [vector-item              view-props]
        (boolean? current-item)                             [boolean-item             view-props]
        (integer? current-item)                             [integer-item             view-props]
        (string? current-item)                              [string-item              view-props]
        (keyword? current-item)                             [keyword-item             view-props]
        (var? current-item)                                 [component-item           view-props]
        (nil? current-item)                                 [nil-item                 view-props]
        :else                                               [unknown-item             view-props]))

(defn database-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ view-props]
  [:div#x-database-browser
    {:style {:width "100%"}}
    [database-item view-props]])

(defn- view
  ; @return (component)
  []
  [components/subscriber ::view
                         {:component  #'database-browser
                          :subscriber [::get-view-props]}])
