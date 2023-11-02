
(ns pretty-elements.expandable.side-effects
    (:require [pretty-elements.expandable.state :as expandable.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle!
  ; @ignore
  ;
  ; @param (keyword) expandable-id
  [expandable-id]
  (let [expanded? (-> @expandable.state/EXPANDED-ELEMENTS expandable-id)]
       (if (nil? expanded?)
           (swap! expandable.state/EXPANDED-ELEMENTS assoc  expandable-id false)
           (swap! expandable.state/EXPANDED-ELEMENTS update expandable-id not))))
