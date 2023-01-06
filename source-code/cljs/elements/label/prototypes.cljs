
(ns elements.label.prototypes
    (:require [candy.api        :refer [param]]
              [hiccup.api       :as hiccup]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ; {:content (metamorphic-content)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :target-id (keyword)(opt)}
  ;
  ; @return (map)
  ; {:color (keyword or string)
  ;  :content (string)
  ;  :copyable? (boolean)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :line-height (keyword)
  ;  :selectable? (boolean)
  ;  :target-id (string)}
  [{:keys [content font-size icon target-id] :as label-props}]
  ; XXX#7009
  ; A label elem prototípus függvénye alkalmazza az elem tartalmán az x.components/content
  ; függvényt, így azt elég egyszer alkalmazni és nem szükséges a különböző vizsgálatok
  ; előtt több helyen is használni!
  ; Pl.: Az elem tartalmának ürességét több helyen szükséges vizsgálni, amihez szükséges
  ;      lenne több helyen alkalmazni az x.components/content függvényt.
  (let [content (x.components/content content)]
       (merge {:color            :default
               :font-size        :s
               :font-weight      :bold
               :horizontal-align :left
               :line-height      :normal
               :selectable?      false}
              (if icon {:icon-color :default :icon-family :material-icons-filled
                        :icon-size (or font-size :s)})
              (param label-props)
              {:content content}
              (if target-id {:target-id (hiccup/value target-id "input")})
              (if (empty? content)
                  {:copyable? false}))))
