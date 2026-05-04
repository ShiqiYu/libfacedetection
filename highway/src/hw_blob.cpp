#include "hw_blob.h"

#include <algorithm>

namespace fdt {
namespace hw {

namespace {

int PaddedChannels(int channels) {
    const int lanes = std::max(HwFloatLanes(), IntrinsicsFloatLanes());
    if (lanes <= 1) {
        return channels;
    }
    return ((channels + lanes - 1) / lanes) * lanes;
}

}  // namespace

HwBlob::HwBlob()
    : rows_(0), cols_(0), channels_(0), channel_step_(0), data_() {}

HwBlob::HwBlob(int rows, int cols, int channels)
    : rows_(0), cols_(0), channels_(0), channel_step_(0), data_() {
    Resize(rows, cols, channels);
}

void HwBlob::Resize(int rows, int cols, int channels) {
    rows_ = rows;
    cols_ = cols;
    channels_ = channels;
    channel_step_ = PaddedChannels(channels);
    data_.assign(static_cast<size_t>(rows_) * cols_ * channel_step_, 0.0f);
}

void HwBlob::ResizeForOverwrite(int rows, int cols, int channels) {
    rows_ = rows;
    cols_ = cols;
    channels_ = channels;
    channel_step_ = PaddedChannels(channels);
    data_.resize(static_cast<size_t>(rows_) * cols_ * channel_step_);
}

void HwBlob::Clear() {
    std::fill(data_.begin(), data_.end(), 0.0f);
}

bool HwBlob::Empty() const {
    return rows_ <= 0 || cols_ <= 0 || channels_ <= 0 || data_.empty();
}

BlobView HwBlob::View() {
    BlobView view;
    view.data = data_.empty() ? 0 : data_.data();
    view.rows = rows_;
    view.cols = cols_;
    view.channels = channels_;
    view.channel_step = channel_step_;
    return view;
}

ConstBlobView HwBlob::View() const {
    ConstBlobView view;
    view.data = data_.empty() ? 0 : data_.data();
    view.rows = rows_;
    view.cols = cols_;
    view.channels = channels_;
    view.channel_step = channel_step_;
    return view;
}

float* HwBlob::Ptr(int row, int col) {
    return data_.data() + (static_cast<size_t>(row) * cols_ + col) *
                              static_cast<size_t>(channel_step_);
}

const float* HwBlob::Ptr(int row, int col) const {
    return data_.data() + (static_cast<size_t>(row) * cols_ + col) *
                              static_cast<size_t>(channel_step_);
}

}  // namespace hw
}  // namespace fdt
