
(ns pretty-elements.expandable.env
    (:require [pretty-elements.expandable.state :as expandable.state]
              [logic.api                 :refer [nonfalse?]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expanded?
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  ;
  ; @return (boolean)
  [expandable-id]
  (-> @expandable.state/EXPANDED-ELEMENTS expandable-id nonfalse?))
