
(ns elements.expandable.helpers
    (:require [elements.expandable.state :as expandable.state]
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

(defn toggle!
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  [expandable-id]
  (let [expanded? (-> @expandable.state/EXPANDED-ELEMENTS expandable-id)]
       (if (nil? expanded?)
           (swap! expandable.state/EXPANDED-ELEMENTS assoc  expandable-id false)
           (swap! expandable.state/EXPANDED-ELEMENTS update expandable-id not))))
