
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.line-diagram.views
    (:require [css.api                          :as css]
              [elements.line-diagram.helpers    :as line-diagram.helpers]
              [elements.line-diagram.prototypes :as line-diagram.prototypes]
              [random.api                       :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-diagram-section
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (map) section-props
  [diagram-id diagram-props section-props]
  [:div.e-line-diagram--section (line-diagram.helpers/diagram-section-attributes diagram-id diagram-props section-props)])

(defn- line-diagram-sections
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)
  ;   :strength (px)}
  [diagram-id {:keys [sections strength] :as diagram-props}]
  (letfn [(f [sections section-props]
             (let [section-props (line-diagram.prototypes/section-props-prototype section-props)]
                  (conj sections [line-diagram-section diagram-id diagram-props section-props])))]
         [:div.e-line-diagram--sections {:style {:height (css/px strength)}}
                                        (reduce f [:<>] sections)]))

(defn line-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-line-diagram (line-diagram.helpers/diagram-attributes diagram-id diagram-props)
                       [line-diagram-sections                   diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ;  {:indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :sections (maps in vector)}
  ;    [{:color (keyword or string)(opt)
  ;       :default, :highlight, :muted, :primary, :secondary, :success, :warning
  ;       Default: primary
  ;      :label (metamorphic-content)(opt)
  ;       TODO ...
  ;      :value (integer)}]
  ;   :strength (px)(opt)
  ;     Default: 2
  ;     Min: 1
  ;     Max: 6
  ;   :total-value (integer)(opt)
  ;    Default: A szakaszok aktuális értékének összege}
  ;
  ; @usage
  ;  [line-diagram {...}]
  ;
  ; @usage
  ;  [line-diagram :my-line-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (line-diagram.prototypes/diagram-props-prototype diagram-props)]
        [line-diagram diagram-id diagram-props])))