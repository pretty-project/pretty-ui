
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.07.18
; Description:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.core
    (:require [app-fruits.dom        :as dom]
              [app-fruits.reagent    :as reagent :refer [ratom]]
              [mid-fruits.candy      :refer [param return]]
              [mid-fruits.form       :as form]
              [mid-fruits.gestures   :as gestures]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.loop       :as loop :refer [reduce-indexed reduce-while reduce-while-indexed]]
              [mid-fruits.map        :as map]
              [mid-fruits.math       :refer [calc]]
              [mid-fruits.mixed      :as mixed]
              [mid-fruits.pretty     :as pretty]
              [mid-fruits.random     :as random]
              [mid-fruits.string     :as string]
              [mid-fruits.time       :as time]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-developer.api   :as developer]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-plugins.api     :as plugins]
              [x.app-sync.api        :as sync]
              [x.app-ui.api          :as ui]

              ; TEMP
              ;[extensions.clients.api]
              [extensions.media-storage.api]
              [cljs-time.core :as cljs-time.core]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  [db _ a]
  {:debug a
   :x (r x.app-ui.surface-geometry/get-surface-header-height db ::view)})

(a/reg-sub ::get-view-props get-view-props)

(a/redirect-sub ::get-view-props :xxkk)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :playground/test!
  (fn [{:keys [db]} _]))



;; -- Case A ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :handle-apple_!
  (fn [{:keys [db]} [_ apple]]
      {:db (r db/set-item! [:apple] apple)}))

(a/reg-event-fx
  :handle-banana_!
  (fn [{:keys [db]} [_ banana]]
      {:db (r db/set-item! [:banana] banana)}))

(a/reg-event-fx
  :handle-query-response_!
  (fn [_ [_ server-response]]
      {:dispatch-n [[:handle-apple_!  (get server-response :apple)]
                    [:handle-banana_! (get server-response :banana)]]}))

(a/reg-event-fx
  :send-request_!
  (fn [_ _]
      [:x.app-sync/send-request! {:on-success [:handle-request-response_!]}]))



;; -- Case B ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :handle-apple-!
  (fn [{:keys [db]} [_ apple]]
      {:db (r db/set-item! [:apple] apple)}))

(a/reg-event-fx
  :handle-banana-!
  (fn [{:keys [db]} [_ banana]]
      {:db (r db/set-item! [:banana] banana)}))

(a/reg-event-fx
  :send-request-!
  (fn [_ _]
      [:x.app-sync/send-request! {:target-events {:apple  [:handle-apple-!]
                                                  :banana [:handle-banana-!]}}]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- local-state
  []
  (let [state (ratom 10)]
        ;unix-timestamp (time/timestamp)]
       (fn []
           [:div "Here comes the sun"
            [:div (str (vector/inject-item [] 5 "a"))]])))

(defn xxxx
  [my-number]
  (if (and (integer? my-number)
           (even?    my-number))
      [elements/text-field ::my-number-field
                           {:label "My even number" :value-path [:my-number]
                            :on-blur [:x.app-ui/blow-bubble! {:content (str "My number: " my-number " is even")}]}]
      [elements/text-field ::my-number-field
                           {:label "My odd number" :value-path [:my-number]
                            :on-blur [:x.app-ui/blow-bubble! {:content (str "My number: " my-number " is odd")}]}]))

(defn my-number
  []
  (let [my-number (a/subscribe [:x.app-db/get-item [:my-number]])]
       (fn []
           [:div (str "My number: " @my-number)
                 [:br]
                 (random-uuid)
                 (if (and (integer? @my-number)
                          (even?    @my-number))
                     [xxxx @my-number]
                     [xxxx @my-number])])))

(a/reg-event-fx
  :heyho!
  (fn [_ v]
      (println (str v))))

(defn- playground
  []
  [:<> [elements/card {:content "HEY CARD"}]
       [:div (str "timestamp:" (cljs-time.core/hour (cljs-time.core/now)))
             [:br]
             (str "timestamp:" (time/get-hours-2))]
       [elements/select {:label "My select"
                         :on-select [:heyho!]
                         :options [{:label "Option #1" :value :option-1}
                                   {:label "Option #2" :value :option-2}]}]

       [elements/button {:label "Browse files!"
                         :on-click [:file-browser/load! {:value-path [:tedt :a]
                                                         :browser-mode :add-files}]}]

       [elements/switch {:label "My switch"}]

       [elements/separator {:size :xxl}]
       [elements/button {:label "Inc!" :on-click [:x.app-db/apply! [:my-number] inc]}]
       [my-number]
       [elements/separator {:size :xxl}]

       [elements/date-field {:label "My date field" :value-path [:my :value]}]
       [elements/select {:label "My select"
                         :options [{:label "Option #1" :value :op-1}
                                   {:label "Option #2" :value :op-2}]}]
       [elements/checkbox {}] ;{:label "My checkbox"}]
       [elements/counter {:label "My counter" :resetable? true :initial-value 420}]
       [elements/button {:label "My button" :on-click [:x.app-db/set-item! [:hu] true]}]
       [elements/radio-button {:label "My radio-button"
                               :unselectable? true
                               :options [{:label "Option #1" :value "ot-1"}
                                         {:label "Option #2" :value "ot-2"}]}]
       [elements/anchor {:href "/x" :content "xxx"}]
       [elements/text-field {:label "My text-field" :emptiable? true
                             :surface {:content [:<> [:div {:style {:width "200px" :height "300px"}}]]}
                             ;:modifier #(string/starts-with! % "/")
                             :start-adornments [{:icon :search}]
                             :placeholder "Placeholder"
                             :validator {:f #(= % "x")
                                         :invalid-message "heyho"}}]
       [elements/multiline-field {:label "My textarea" :placeholder "Placeholder"}]
       [elements/date-field {:label "My date field"}]
       [elements/chip {:color :highlight :label "My chip" :variant :outlined :on-delete [:a]}]])

(defn debug
  [_ {:keys [debug x]}]
  [:div (str "debug:" debug)
        [:br]
        [:pre (pretty/mixed->string debug)]
        [:br]
        "x: " (str x)])

(a/reg-event-db :my-extender (fn [db [_ field-id value]]
                                 (update-in db [:my-options] vector/conj-item {:name value})))

(defn form
  [_ _]
  [:<> [elements/text-field {:label "TF" :emptiable? true}]
       [elements/expandable {:content "My content" :icon :apps :label "My expandable"}]
       [elements/multi-field :mf
                             {:label "MF"
                              :value-path [:mf]}]

       [elements/combo-box :cb
                           {:label "CB"
                            ;:extendable? true
                            :get-label-f  #(do (get % :x))
                            :options-path [:debug-mcb :options]
                            :value-path   [:debug-cb  :value]}]

       [elements/multi-combo-box :mcb
                                 {:label "MCB"
                                  ;:extendable? true
                                  :get-label-f #(get % :x)
                                  :options-path [:debug-mcb :options]
                                  :value-path   [:debug-mcb :value]}]])

(defn hello
  [sortable-id item-dex item]
  [:div
    {:style {:height "72px" :display :flex :flex-directory :column :justify-content :center
             :align-items :center
             :border "1px solid black"
             :user-select :none
             :background :white}}
    (str "sortable-id: " sortable-id)
    [:br] (str "render-dex: "  item-dex)
    [:br] (str "item: "        item)])

(defn- sortable
  [_ _]
  [:<> ;[plugins/sortable
        ; {:element #'hello
        ;  :partition-id ::dbg

       [elements/button {:label "Add more items!"
                         :on-click [:x.app-db/apply! [::dbg] vector/concat-items ["0000" "_____" "****"]]}]

       [elements/button {:label "Add more item!"
                         ;:on-click [:x.app-db/apply! [::dbg] vector/conj-item "p"]}]
                         :on-click [:x.app-plugins/add-sortable-item! :my-sortable "####"]}]

       [elements/button {:label "Remove 2nd item!"
                         :on-click [:x.app-plugins/remove-sortable-item! :my-sortable 1]}]

       [plugins/sortable-2
         :my-sortable
         {:element #'hello
          :value-path [::dbg]}]])

(defn- view
  []
  [:<> [:div {:style {:position :sticky :top "100px"}}
             "Sticky"]
       [:div "id->placeholder"
             [:br]
             (str (sync/id->placeholder "my-id"))]
       [elements/box {:content #'playground
                      :icon    :sports_esports
                      :label   "Playground"
                      :horizontal-align :left}]
                      ;:border-color :primary}]
       [elements/box {:content #'local-state
                      ;:label   "Local state"
                      :horizontal-align :left
                      :expandable? true}]
       [elements/box {:content "my-card"
                       :label "My card"}]
                       ;:ghost-view? true}]
       [elements/box {:content #'debug
                      :label   "Debug"
                      :horizontal-align :left
                      :subscriber [::get-view-props]
                      :on-click [:x.app-ui/blow-bubble! {:content "Hey-ho"}]}]
       [elements/box {:content #'form
                      :label   "Form"
                      :horizontal-align :left}]
                      ;:subscriber [::xxkk]}]
       [elements/box {:content #'sortable
                      :label   "Sortable"
                      :horizontal-align :left
                      :subscriber [::get-view-props]}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  [:x.app-ui/set-surface!
   ::view
   {:content     #'view
   ;:control-sidebar {:content #'control-sidebar}
    :control-bar   {:content #'ui/go-back-surface-label-bar
                    :content-props {:label "Playground"}}
;    :label-bar   {:content #'ui/go-back-surface-label-bar}
;                  :content-props {:label "Playground"}}
    :initializer ;[:x.app-db/set-item! [:dbg] ["a" "b" "c"]]}])
                 ;[:x.app-db/set-item! [::dbg] [{:a "0"} {:b "1"} {:c "2"}]]}])
                 ;[:x.app-db/set-item! [:B] [{:x "a"} {:x "aa"} {:x "b"}]]}])
                 [:x.app-db/set-item! [:debug-mcb :options] [{:x "Apple"}
                                                             {:x "Apple juice"}
                                                             {:x "Pineapple"}
                                                             {:x "Banana"}
                                                             {:x "Brown"}
                                                             {:x "Apocalypse"}]]}])

(a/reg-event-fx
  ::load!
  {:dispatch-n [[::render!]]})

(a/reg-event-fx
  ::initialize!
  {:dispatch-n [[:x.app-router/add-route!
                 ::route
                 {:route-event    [::load!]
                  :route-template "/playground"
                  :restricted?    true
                  :route-title    "Playground"}]]})
;               [:x.app-environment.css-handler/add-external-source! "css/playground/site.css"]

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
