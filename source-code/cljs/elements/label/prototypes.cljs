
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.label.prototypes
    (:require [candy.api        :refer [param]]
              [x.components.api :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ;  {:content (metamorphic-content)(opt)
  ;   :icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword or string)
  ;   :content (string)
  ;   :copyable? (boolean)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)
  ;   :line-height (keyword)
  ;   :selectable? (boolean)}
  [{:keys [content icon] :as label-props}]
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
              (if icon {:icon-family :material-icons-filled})
              (param label-props)
              {:content content}
              (if (empty? content)
                  {:copyable? false}))))
