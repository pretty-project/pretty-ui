
(ns tools.image-loader.sample
    (:require [tools.image-loader.api :as image-loader]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-image
  []
  [image-loader/component {:uri "/my-image.png"}])
