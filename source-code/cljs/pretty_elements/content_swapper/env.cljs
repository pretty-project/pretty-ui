
(ns pretty-elements.content-swapper.env
    (:require [pretty-elements.content-swapper.state :as content-swapper.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-active-page
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ;
  ; @return (metamorphic-content)
  [swapper-id]
  (let [active-page-id (get-in @content-swapper.state/SWAPPERS [swapper-id :active-page])
        page-pool      (get-in @content-swapper.state/SWAPPERS [swapper-id :page-pool])]
       (letfn [(f [{:keys [id page]}] (if (= id active-page-id) page))]
              (some f page-pool))))
