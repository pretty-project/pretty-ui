
(ns pretty-diagrams.core.props
    (:require [pretty-elements.core.props]
              [fruits.noop.api :refer [none return]]
              [pretty-defaults.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.core.props/*)
(def row-props pretty-elements.core.props/row-props)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-props
  ; @ignore
  ;
  ; @description
  ; Applies the default data properties on the given 'diagram-props' map.
  ;
  ; @param (map) diagram-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [diagram-props & [default-props]]
  (-> diagram-props (utils/use-default-values {:datum-color-f none
                                               :datum-label-f none
                                               :datum-value-f return}
                                              (-> default-props))))

(defn stroke-props
  ; @ignore
  ;
  ; @description
  ; Applies the default stroke properties on the given 'diagram-props' map.
  ;
  ; @param (map) diagram-props
  ; @param (map)(opt) default-props
  ;
  ; @return (map)
  ; {}
  [diagram-props & [default-props]]
  (-> diagram-props (utils/use-default-values {}
                                              (-> default-props))))
