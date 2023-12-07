
(ns pretty-elements.expandable.env
    (:require [fruits.logic.api                 :refer [nonfalse?]]
              [pretty-elements.expandable.state :as expandable.state]))

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
