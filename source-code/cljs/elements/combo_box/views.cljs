
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.combo-box.views
    (:require [elements.combo-box.helpers    :as combo-box.helpers]
              [elements.combo-box.prototypes :as combo-box.prototypes]
              [elements.text-field.helpers   :as text-field.helpers]
              [elements.text-field.views     :as text-field.views]
              [loop.api                      :refer [reduce-indexed]]
              [plugins.reagent.api           :as reagent]
              [random.api                    :as random]
              [re-frame.api                  :as r]
              [x.components.api              :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- default-option-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:option-label-f (function)}
  ; @param (map) option
  [_ {:keys [option-label-f]} option]
  [:div.e-combo-box--option-label (-> option option-label-f x.components/content)])

(defn- combo-box-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:option-component (component)(opt)}
  ; @param (integer) option-dex
  ; @param (map) option
  [box-id {:keys [option-component] :as box-props} option-dex option]
  ; BUG#2105
  ;  A combo-box elemhez tartozó surface felületen történő on-mouse-down esemény
  ;  a mező on-blur eseményének triggerelésével jár, ami a surface felület
  ;  React-fából történő lecsatolását okozná.
  [:button.e-combo-box--option {:on-mouse-down #(do (.preventDefault %))
                                :on-mouse-up   #(do (r/dispatch [:elements.combo-box/select-option! box-id box-props option]))
                               ;:data-selected ...
                                :data-highlighted (= option-dex (combo-box.helpers/get-highlighted-option-dex box-id))}
                               (if option-component [option-component         box-id box-props option]
                                                    [default-option-component box-id box-props option])])

(defn- combo-box-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  ; XXX#3270 (source-code/cljs/elements/combo-box/helpers.cljs)
  (let [options (combo-box.helpers/get-rendered-options box-id box-props)]
       (letfn [(f [option-list option-dex option]
                  ;^{:key (random/generate-react-key)}
                  (conj option-list [combo-box-option box-id box-props option-dex option]))]
              [:div.e-combo-box--options {:data-options-rendered (-> options empty? not)
                                          :data-scrollable-y     true}
                                         (reduce-indexed f [:<>] options)])))

(defn- combo-box-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div.e-combo-box--surface [combo-box-options box-id box-props]])

(defn- combo-box-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [box-props (assoc box-props :surface [combo-box-surface box-id box-props])]
       [text-field.views/element box-id box-props]))

(defn- combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (combo-box.helpers/combo-box-did-mount box-id box-props))
                       :reagent-render      (fn [_ box-props] [combo-box-structure box-id box-props])}))

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; A combo-box elem alapkomponense a text-field elem.
  ; A combo-box elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ;  {:field-content-f (function)(opt)
  ;    Default: return
  ;   :field-value-f (function)(opt)
  ;    Default: return
  ;   :initial-options (vector)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-component (component)(opt)
  ;    Default: elements.combo-box.views/default-option-component
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
  ;   :options (vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @usage
  ;  [combo-box {...}]
  ;
  ; @usage
  ;  [combo-box :my-combo-box {...}]
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (let [box-props (combo-box.prototypes/box-props-prototype  box-id box-props)]
        ; WARNING! DEPRECATED! DO NOT USE!
        ;box-props (combo-box.prototypes/box-events-prototype box-id box-props)
        [combo-box box-id box-props])))
