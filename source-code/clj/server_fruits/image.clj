(ns server-fruits.image
  (:import [javax.imageio ImageIO])
  (:import [javax.imageio.plugins.jpeg JPEGImageWriteParam])
  (:import [javax.imageio.stream FileImageOutputStream])
  (:import [javax.imageio IIOImage])
  (:import [javax.imageio ImageWriteParam])

  (:import [java.awt.image BufferedImage])
  (:import [java.awt AlphaComposite])
  (:import [java.awt Image])
  (:import [java.io  File]))


(defn get-scaled-dimension [[width height] wanted-width]
      (let [ratio (/ wanted-width width)
            new-height (* ratio height)]
           [wanted-width new-height]))

(defn resize-img [img type-int wanted-width]
      (let [original-dimension [(.getWidth img)  (.getHeight img)]
            [new-width new-height] (get-scaled-dimension original-dimension wanted-width)
            resized (new BufferedImage new-width new-height type-int);BufferedImage/TYPE_INT_ARGB)
            g2d (.createGraphics resized)
            tmp (.getScaledInstance img new-width new-height Image/SCALE_SMOOTH)]

        (.drawImage g2d tmp 0 0 nil)
        (.dispose g2d)
        resized))


(defn save-in-good-quality [idk-img output-path quality]
      (let [jpeg-params (new JPEGImageWriteParam nil)
            mime-type   (last (clojure.string/split output-path #"\."))
            writer      (.next (ImageIO/getImageWritersByFormatName mime-type))
            set-output  (.setOutput writer
                          (new FileImageOutputStream
                               (clojure.java.io/file output-path)))]

           (.setCompressionMode jpeg-params ImageWriteParam/MODE_EXPLICIT)
           (.setCompressionQuality jpeg-params quality)
           (.write writer nil (new IIOImage idk-img nil nil) jpeg-params)))

(defn make-image [input-path output-path & [config]]
  (let [input-img (ImageIO/read (clojure.java.io/file input-path))
        img-width (.getWidth input-img)

        mime-type (last (clojure.string/split output-path #"\."))
        type-int  (if (= "png" mime-type)
                    BufferedImage/TYPE_INT_ARGB
                    BufferedImage/TYPE_INT_RGB)
        input     (if (> 1600 img-width)
                    input-img
                    (resize-img input-img type-int
                        (get config :width 1600)))

        [x y]     [(.getWidth input)  (.getHeight input)]
        new-img   (new BufferedImage x y type-int)
        g2d       (.getGraphics new-img)]

    (.drawImage g2d input 0 0 nil)
    (save-in-good-quality new-img output-path
                          (get config :quality 1.0))
    (.dispose g2d)

    (clojure.java.io/file output-path)))
