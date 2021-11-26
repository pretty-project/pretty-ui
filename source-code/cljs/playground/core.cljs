
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
              [x.app-layouts.api     :as layouts]
              [x.app-locales.api     :as locales]
              [x.app-sync.api        :as sync]
              [x.app-tools.api       :as tools]
              [x.app-ui.api          :as ui]

              ; TEMP
              [cljs-time.core :as cljs-time.core]

              ; A project-emulator.core fájl közös használatának mellőzése miatt a fejlesztés alatt
              ; lévő modulok behívása a playground.core névtérben történik.
              [extensions.clients.api]
              [extensions.home.api]
              [extensions.media.api]
              [extensions.products.api]
              [extensions.settings.api]))



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
       [tools/infinite-loader :my-loader {:on-viewport #(println "Playground infinite loader in viewport again!")}]
       [elements/button ::reload-infinite-loader-button
                        {:label "Reload infinite loader!" :on-click [:tools/reload-infinite-loader! :my-loader]
                         :variant :transparent :color :secondary}]])

(defn buttons
  []
  [:<> [elements/anchor {:content "My anchor link"   :href "/my-link"}]
       [elements/anchor {:content "My anchor button" :on-click [:router/go-to! "/my-link"]}]
       [elements/button ::pres-esc-button
                        {:label "Press ESC" :keypress {:key-code 27} :layout :icon-button
                         :variant :transparent :color :none :icon :people}]
       [elements/button ::my-button
                        {:label "My button" :variant :filled :color :primary :icon :people}]])

(defn chips
  []
  [:<> [elements/chip {:label "My chip" :icon :apps}]
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
  [:<> [elements/select ::my-select
                        {:label "My select"
                         :on-select #(println "Selected")
                         :options [{:label "Option #1" :value :option-1}
                                   {:label "Option #2" :value :option-2}]}]
       [elements/date-field ::my-date-field
                            {:label "My date field" :value-path (db/path ::stuff :my-date)}]
       [elements/text-field ::my-text-field-w-surface
                            {:label "My text-field w/ surface" :emptiable? true
                             :placeholder "Placeholder"
                             :surface {:content [:<> [:div {:style {:padding "24px 12px"}} "Text field surface"]]}
                             :helper "My helper"}]
       [elements/text-field ::my-text-field-w-modifier
                            {:label "My text-field w/ modifier" :emptiable? true
                             :placeholder "Placeholder"
                             :modifier #(string/starts-with! % "/")}]
       [elements/text-field ::my-text-field-w-validator
                            {:label "My text-field w/ validator" :emptiable? true
                             :placeholder "Placeholder"
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""}}]
       [elements/text-field ::my-text-field-w-prevalidator
                            {:label "My text-field w/ prevalidator" :emptiable? true
                             :placeholder "Placeholder"
                             :initial-value "x"
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""
                                         :pre-validate? true}}]
       [elements/password-field ::my-password-field-w-adornments
                                {:label "My password-field w/ adornments" :emptiable? true
                                 :placeholder "Placeholder"
                                 :start-adornments [{:icon :sentiment_very_satisfied :on-click [] :tooltip "Hello"}]}]
       [elements/multiline-field ::my-multiline-field
                                 {:label "My multiline-field" :placeholder "Placeholder"}]])

(defn- form-b
  []
  [:<> [elements/switch ::my-switch
                        {:label "My switch"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :default-value true}]
       [elements/switch ::your-switch
                        {:label "Your switch"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/label {:content "My label"}]
       [elements/horizontal-line {:color :highlight}]
       [elements/checkbox ::my-checkbox
                          {:label "My checkbox"
                           :helper "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/checkbox ::your-checkbox
                          {:label "Your checkbox"
                           :helper "Check it to check out!"}]
       [elements/checkbox ::our-checkbox
                          {:label "Our checkbox"
                           :helper "Check it to check out!"}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/label {:content "Your label"}]
       [elements/horizontal-line {:color :highlight}]
       [elements/counter ::my-counter
                         {:label "My counter" :resetable? true :initial-value 420}]
       [elements/counter ::your-counter
                         {:label "Your counter" :resetable? true :initial-value 420
                          :helper "Your helper"}]
       [elements/separator {:orientation :horizontal :size :l}]
       [elements/label {:content "Our label"}]
       [elements/horizontal-line {:color :highlight}]
       [elements/anchor {:href "/x" :content "My anchor"}]
       [elements/button ::my-button-w-helper
                        {:label "My button"
                         :helper "Your helper"}]
       [elements/button ::browse-files-button
                        {:label "Browse files!"
                         :on-click [:file-browser/load! {:value-path (db/path ::stuff :selected-files)
                                                         :browser-mode :add-files}]}]
       [elements/separator {:orientation :horizontal :size :xxl}]
       [elements/radio-button ::my-radio-button
                              {:label "My radio-button"
                               :unselectable? true
                               :get-label-f :label
                               :initial-options [{:label "Option #1" :value "ot-1"}
                                                 {:label "Option #2" :value "ot-2"}]}]
       [elements/chip  {:color :highlight :label "My chip" :variant :outlined :on-delete [:chip-deleted :layout :fit]}]
       [elements/chips ::my-chips
                       {:label "My chips" :chips [{:label "Chip #1" :variant :outlined}
                                                  {:label "Chip #2" :variant :filled}]
                                          :on-delete []}]])

(defn form-c
  [_ _]
  [:<> [elements/expandable  {:content "My content" :icon :apps :label "My expandable"}]
       [elements/multi-field ::my-multi-field
                             {:label "My multi-field"
                              :value-path [:x :y]}]
       [elements/combo-box ::my-combo-box
                           {:label "My combo-box"
                            :extendable? true
                            :get-label-f  #(do (get % :x))
                            :options-path (db/path ::stuff :combo-box :options)
                            :initial-options [{:x "A"} {:x "B"}]
                            :initial-value {:x "B"}}]

       [elements/multi-combo-box ::my-multi-combo-box
                                 {:label "My multi-combo-box"
                                  :extendable? true
                                  :get-label-f #(get % :x)
                                  ;:options-path (db/path ::stuff :multi-combo-box :options)
                                  :initial-options [{:x "A"} {:x "B"}]}]])

(defn- menu-bar
  []
  [elements/menu-bar {:menu-items [{:label "Menu item #1" :on-click [] :color :default}
                                   {:label "Menu item #2" :on-click [] :color :muted}
                                   {:label "Menu item #3" :on-click [] :color :muted}]}])

(defn- view
  []
  [:<> [elements/box {:body   {:content "Menu"}
                      :header {:content #'menu-bar
                               :sticky? true}}]
       [elements/box {:body   {:content #'buttons}
                      :header {:content "Buttons"
                               :sticky? true}
                      :horizontal-align :left
                      :stickers [{:icon :apps :tooltip :filters :on-click []}]}]

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
    :initializer [:x.app-db/set-item! (db/path ::stuff :multi-combobox :options)
                                      [{:x "Apple"}
                                       {:x "Apple juice"}
                                       {:x "Pineapple"}
                                       {:x "Banana"}
                                       {:x "Brown"}
                                       {:x "Apocalypse"}]]}])

(a/reg-event-fx
  ::load!
  {:dispatch-n [[:x.app-ui/set-header-title! "Playground"]
                [:x.app-ui/set-window-title! "Playground"]
                [::render!]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:router/add-route! ::route
                                    {:route-event    [::load!]
                                     :route-template "/playground"
                                     :restricted?    true}]})
