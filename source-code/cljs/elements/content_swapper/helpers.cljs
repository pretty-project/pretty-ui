
(ns elements.content-swapper.helpers
    (:require [elements.content-swapper.state :as content-swapper.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-swapping-content
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {}
  ;
  ; @return (metamorphic-content)
  [swapper-id {:keys [pages]}]
  (if-let [active-dex (-> @content-swapper.state/SWAPPERS swapper-id :active-dex)]
          (get-in pages [active-dex :content])))
