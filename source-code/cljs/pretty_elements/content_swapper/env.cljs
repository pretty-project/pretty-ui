
(ns pretty-elements.content-swapper.env
    (:require [pretty-elements.content-swapper.state :as content-swapper.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-active-content
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ;
  ; @return (metamorphic-content)
  [swapper-id]
  (let [active-content-id (get-in @content-swapper.state/SWAPPERS [swapper-id :active-content])
        content-pool      (get-in @content-swapper.state/SWAPPERS [swapper-id :content-pool])]
       (letfn [(f0 [{:keys [id content]}] (if (= id active-content-id) content))]
              (some f0 content-pool))))
