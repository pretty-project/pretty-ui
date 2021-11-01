
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
              [x.app-sync.api        :as sync]
              [x.app-ui.api          :as ui]

              ; TEMP
              ;[extensions.clients.api]
              [extensions.media-storage.api]
              [cljs-time.core :as cljs-time.core]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  [db _]
  {})

(a/reg-sub ::get-view-props get-view-props)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :playground/test!
  (fn [{:keys [db]} _]
      {:dispatch [:x.test!]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- local-state
  []
  (let [state (ratom 10)]
       (fn [] [:div "Here comes the sun"])))

(defn- infinite-loader
  []
  [:<> [elements/text {:content "Infinite loader printed to console" :color :highlight :font-size :xs}]
       [components/infinite-loader :my-loader {:on-viewport #(println "Playground infinite loader in viewport again!")}]
       [elements/button {:label "Reload infinite loader!" :on-click [:x.app-components/reload-infinite-loader! :my-loader]
                         :variant :transparent :color :secondary}]])

(defn buttons
  []
  [:<> [elements/anchor {:content "My anchor link"   :href "/my-link"}]
       [elements/anchor {:content "My anchor button" :on-click [:x.app-router/go-to! "/my-link"]}]
       [elements/button {:label "Press ESC" :keypress {:key-code 27} :layout :icon-button
                         :variant :transparent :color :none :icon :people}]
       [elements/button {:label "My button" :variant :filled :color :primary :icon :people}]])

(defn chips
  []
  [:<> [elements/chip {:label "My chip"}]
       [elements/chip {:label "Your chip" :variant :outlined}]
       [elements/chip {:label "Your chip" :variant :outlined :on-delete [:x.test!]}]])

(defn diagrams
  []
  [:<> [elements/circle-diagram {:sections [{:color :primary :value 50}
                                            {:color :highlight :value 20}]}]
       [elements/line-diagram {:sections [{:color :primary :value 50}
                                          {:color :highlight :value 20}]}]])

(defn content-bar
  []
  [elements/content-bar {}])

(defn- form-a
  []
  [:<> [elements/select {:label "My select"
                         :on-select #(println "Selected")
                         :options [{:label "Option #1" :value :option-1}
                                   {:label "Option #2" :value :option-2}]}]
       [elements/date-field {:label "My date field" :value-path (db/path ::stuff :my-date)}]
       [elements/text-field {:label "My text-field w/ surface" :emptiable? true
                             :placeholder "Placeholder"
                             :surface {:content [:<> [:div {:style {:padding "24px 12px"}} "Text field surface"]]}
                             :helper "My helper"}]
       [elements/text-field {:label "My text-field w/ modifier" :emptiable? true
                             :placeholder "Placeholder"
                             :modifier #(string/starts-with! % "/")}]
       [elements/text-field {:label "My text-field w/ validator" :emptiable? true
                             :placeholder "Placeholder"
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""}}]
       [elements/text-field {:label "My text-field w/ prevalidator" :emptiable? true
                             :placeholder "Placeholder"
                             :initial-value "x"
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""
                                         :pre-validate? true}}]
       [elements/password-field {:label "My password-field w/ adornments" :emptiable? true
                                 :placeholder "Placeholder"
                                 :start-adornments [{:icon :sentiment_very_satisfied}]}]
       [elements/multiline-field {:label "My textarea" :placeholder "Placeholder"}]])

(defn- form-b
  []
  [:<> [elements/horizontal-line {:color :highlight}]
       [elements/separator {:orientation :horizontal :size :m}]
       [elements/switch {:label "My switch"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :default-value true}]
       [elements/switch {:label "Your switch"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/label {:content "My label"}]
       [elements/horizontal-line {:color :highlight}]
       [elements/checkbox {:label "My checkbox"
                           :helper "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/checkbox {:label "Your checkbox"
                           :helper "Check it to check out!"}]
       [elements/checkbox {:label "Our checkbox"
                           :helper "Check it to check out!"}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/label {:content "Your label"}]
       [elements/horizontal-line {:color :highlight}]
       [elements/counter :my-counter {:label "My counter" :resetable? true :initial-value 420}]
       [elements/counter {:label "My counter" :resetable? true :initial-value 420
                          :helper "Your helper"}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/label {:content "Our label"}]
       [elements/horizontal-line {:color :highlight}]
       [elements/anchor {:href "/x" :content "My anchor"}]
       [elements/button {:label "My button"
                         :helper "Your helper"}]
       [elements/button {:label "Browse files!"
                         :on-click [:file-browser/load! {:value-path (db/path ::stuff :selected-files)
                                                         :browser-mode :add-files}]}]
       [elements/separator {:orientation :horizontal :size :xxl}]
       [elements/radio-button {:label "My radio-button"
                               :unselectable? true
                               :options [{:label "Option #1" :value "ot-1"}
                                         {:label "Option #2" :value "ot-2"}]}]
       [elements/chip {:color :highlight :label "My chip" :variant :outlined :on-delete [:chip-deleted :layout :fit]}]
       [elements/chips {:label "My chips" :chips [{:label "Chip #1" :variant :outlined}
                                                  {:label "Chip #2" :variant :filled}]}]])

(defn form-c
  [_ _]
  [:<> [elements/expandable {:content "My content" :icon :apps :label "My expandable"}]
       [elements/multi-field {:label "My multi-field"}]
       [elements/combo-box {:label "My combobox"
                            :extendable? true
                            :get-label-f  #(do (get % :x))
                            :options-path (db/path ::stuff :combobox :options)}]

       [elements/multi-combo-box {:label "My multi-combobox"
                                  :extendable? true
                                  :get-label-f #(get % :x)
                                  :options-path (db/path ::stuff :multi-combobox :options)}]])

(defn- view
  []
  [:<> [elements/content-bar {:layout :header :content [elements/label {:content "Playground" :font-size :l
                                                                        :icon :sports_esports :font-weight :extra-bold}]}]
       [elements/menu-bar {:menu-items [{:label "Menu item #1" :on-click [:x.app-router/go-to! "/valahol"]}
                                        {:label "Menu item #2" :on-click [:x.app-router/go-to! "/sehol"]}
                                        {:label "Menu item #3" :on-click [:x.app-router/go-to! "/akarhol"]}]}]
       [elements/box {:body   {:content #'buttons}
                      :header {:content "Buttons"
                               :sticky? true}
                      :horizontal-align :left
                      :stickers [{:icon :apps}]}]

       [elements/box {:body   {:content #'chips}
                      :header {:content "Chips"
                               :sticky? true}
                      :horizontal-align :left}]
       [elements/box {:body   {:content #'diagrams}
                      :header {:content "Diagrams"
                               :sticky? true}
                      :horizontal-align :left}]
       [elements/box {:body   {:content #'form-a}
                      :header {:content "Form A"
                               :sticky? true}
                      :horizontal-align :left}]
       [elements/box {:body   {:content #'form-b}
                      :header {:content "Form B"
                               :sticky? true}
                      :horizontal-align :left}]
       [elements/box {:body   {:content #'form-c}
                      :header {:content "Form C"
                               :sticky? true}
                      :horizontal-align :left
                      :expandable? true
                      :expanded? true}]
       [elements/box {:body   {:content #'infinite-loader}}]
       [elements/box {:body   {:content #'local-state}}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::render!
  [:x.app-ui/set-surface!
   ::view
   {:content     #'view
    :label-bar   {:content #'ui/go-back-surface-label-bar
                  :content-props {:label "Playground"}}
;    :label-bar   {:content #'ui/go-back-surface-label-bar}
;                  :content-props {:label "Playground"}}
    :initializer [:x.app-db/set-item! (db/path ::stuff :multi-combobox :options)
                                      [{:x "Apple"}
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
